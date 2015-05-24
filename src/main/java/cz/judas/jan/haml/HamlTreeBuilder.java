package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ChainOfInstanceofChecks")
public class HamlTreeBuilder {
    public RootNode buildTreeFrom(String input) {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.DocumentContext documentContext = parser.document();

        return new RootNode(
                doctype(documentContext),
                FluentIterable.from(documentContext.children)
                        .filter(JavaHamlParser.HtmlTagContext.class)
                        .transform(this::tag)
                        .toList()
        );
    }

    private Optional<String> doctype(ParseTree document) {
        ParseTree firstChild = document.getChild(0);
        if (firstChild instanceof JavaHamlParser.DoctypeContext) {
            return Optional.of(firstChild.getChild(2).getText());
        } else {
            return Optional.empty();
        }
    }

    private HamlNode tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        ImmutableList.Builder<RubyHashExpression> attributeBuilder = ImmutableList.builder();
        ImmutableList.Builder<HamlNode> childrenBuilder = ImmutableList.builder();
        String tagName = null;
        RubyExpression content = ConstantRubyExpression.EMPTY_STRING;

        for (ParseTree parseTree : htmlTagContext.children) {
            if (parseTree instanceof JavaHamlParser.TagNameContext) {
                tagName = parseTree.getChild(1).getText();
            } else if (parseTree instanceof JavaHamlParser.AttributeContext) {
                attributeBuilder.add(attributeHash(parseTree.getChild(0)));
            } else if (parseTree instanceof JavaHamlParser.PlainTextContext) {
                content = ConstantRubyExpression.string(parseTree.getText());
            } else if (parseTree instanceof JavaHamlParser.TextContentContext) {
                content = ConstantRubyExpression.string(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.EscapedTextContext) {
                content = ConstantRubyExpression.string(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.CodeContext) {
                RubyExpression expression = expression((ParserRuleContext) parseTree.getChild(2));
                if(parseTree.getChildCount() == 5) {
                    expression = ((MethodCallExpression)expression).withBlock(
                            new BlockExpression(
                                children((ParserRuleContext)parseTree.getChild(4).getChild(1))
                            )
                    );
                }
                return new CodeNode(expression);
            } else if (parseTree instanceof JavaHamlParser.RubyContentContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if (child instanceof JavaHamlParser.ExpressionContext) {
                        content = expression((ParserRuleContext) child);
                    }
                }
            } else if (parseTree instanceof JavaHamlParser.ChildTagsContext) {
                childrenBuilder.addAll(children((ParserRuleContext) parseTree));
            }
        }

        List<RubyHashExpression> attributes = attributeBuilder.build();
        List<HamlNode> children = childrenBuilder.build();

        if (tagName == null && attributes.isEmpty() && children.isEmpty()) {
            return new TextNode(content);
        } else {
            return new HtmlNode(
                    tagName == null ? "div" : tagName,
                    attributes,
                    content,
                    children
            );
        }
    }

    private List<HamlNode> children(ParserRuleContext context) {
        ImmutableList.Builder<HamlNode> childrenBuilder = ImmutableList.builder();
        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.HtmlTagContext) {
                childrenBuilder.add(tag((JavaHamlParser.HtmlTagContext) child));
            }
        }
        return childrenBuilder.build();
    }

    private RubyHashExpression attributeHash(ParseTree node) {
        if (node instanceof JavaHamlParser.ClassAttributeContext) {
            return RubyHashExpression.singleEntryHash(
                    ConstantRubyExpression.symbol("class"),
                    ConstantRubyExpression.string(node.getChild(1).getText())
            );
        } else if (node instanceof JavaHamlParser.IdAttributeContext) {
            return RubyHashExpression.singleEntryHash(
                    ConstantRubyExpression.symbol("id"),
                    ConstantRubyExpression.string(node.getChild(1).getText())
            );
        } else if (node instanceof JavaHamlParser.AttributeHashContext) {
            return rubyAttributeHash((JavaHamlParser.AttributeHashContext) node);
        } else if (node instanceof JavaHamlParser.HtmlAttributesContext) {
            return htmlStyleAttributes((ParserRuleContext) node);
        }
        throw new IllegalArgumentException("Unknown attribute type " + node.getClass());
    }

    private RubyHashExpression htmlStyleAttributes(ParserRuleContext parseTree) {
        ImmutableList.Builder<HashEntry> attributeBuilder = ImmutableList.builder();
        for (ParseTree child : parseTree.children) {
            if (child instanceof JavaHamlParser.HtmlAttributeEntryContext) {
                attributeBuilder.add(htmlStyleAttributeEntry((ParserRuleContext)child));
            }
        }
        return new RubyHashExpression(attributeBuilder.build());
    }

    private HashEntry htmlStyleAttributeEntry(ParserRuleContext parseTree) {
        String key = null;
        RubyExpression value = null;
        for (ParseTree child : parseTree.children) {
            if (child instanceof JavaHamlParser.HtmlAttributeKeyContext) {
                key = child.getText();
            } else if (child instanceof JavaHamlParser.ExpressionContext) {
                value = expression((ParserRuleContext) child);
            }
        }
        return new HashEntry(ConstantRubyExpression.symbol(key), value);
    }

    private RubyHashExpression rubyAttributeHash(JavaHamlParser.AttributeHashContext parseTree) {
        ImmutableList.Builder<HashEntry> hashEntryBuilder = ImmutableList.builder();
        for (ParseTree child : parseTree.children) {
            if (child instanceof JavaHamlParser.NewStyleHashEntriesContext) {
                for (ParseTree grandChild : ((ParserRuleContext) child).children) {
                    if (grandChild instanceof JavaHamlParser.NewStyleHashEntryContext) {
                        hashEntryBuilder.add(hashEntry((JavaHamlParser.NewStyleHashEntryContext) grandChild));
                    }
                }
            } else if (child instanceof JavaHamlParser.OldStyleHashEntriesContext) {
                for (ParseTree grandChild : ((ParserRuleContext) child).children) {
                    if (grandChild instanceof JavaHamlParser.OldStyleHashEntryContext) {
                        hashEntryBuilder.add(hashEntry((JavaHamlParser.OldStyleHashEntryContext) grandChild));
                    }
                }
            }
        }
        return new RubyHashExpression(hashEntryBuilder.build());
    }

    private HashEntry hashEntry(JavaHamlParser.NewStyleHashEntryContext context) {
        String key = null;
        RubyExpression value = null;

        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.AttributeKeyContext) {
                key = child.getChild(0).getText();
            } else if (child instanceof JavaHamlParser.ExpressionContext) {
                value = expression((ParserRuleContext) child);
            }
        }

        return new HashEntry(ConstantRubyExpression.symbol(key), value);
    }

    private HashEntry hashEntry(JavaHamlParser.OldStyleHashEntryContext context) {
        RubyExpression key = null;
        RubyExpression value = null;

        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.KeyExpressionContext) {
                key = expression((ParserRuleContext) child.getChild(0));
            } else if (child instanceof JavaHamlParser.ValueExpressionContext) {
                value = expression((ParserRuleContext) child.getChild(0));
            }
        }

        return new HashEntry(key, value);
    }

    private RubyExpression expression(ParserRuleContext context) {
        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.SymbolContext) {
                ParseTree firstChild = child.getChild(1);
                if(firstChild instanceof JavaHamlParser.SingleQuotedStringContext) {
                    return ConstantRubyExpression.symbol(firstChild.getChild(1).getText());
                } else {
                    return ConstantRubyExpression.symbol(firstChild.getText());
                }
            } else if (child instanceof JavaHamlParser.SingleQuotedStringContext) {
                return ConstantRubyExpression.string(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.DoubleQuotedStringContext) {
                return ConstantRubyExpression.string(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.FieldReferenceContext) {
                return new FieldReferenceExpression(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.MethodCallContext) {
                return methodCall((JavaHamlParser.MethodCallContext) child);
            } else if(child instanceof JavaHamlParser.IntValueContext) {
                return ConstantRubyExpression.integer(Integer.parseInt(child.getText()));
            }
        }
        throw new IllegalArgumentException("Unknown expression type");
    }

    private MethodCallExpression methodCall(JavaHamlParser.MethodCallContext context) {
        RubyExpression target = null;
        String methodName = null;
        List<RubyExpression> arguments = new ArrayList<>();

        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.FieldReferenceContext) {
                target = new FieldReferenceExpression(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.MethodNameContext) {
                String newMethodName = child.getText();
                if (methodName != null) {
                    target = new MethodCallExpression(target, methodName, arguments);
                    arguments.clear();
                }
                methodName = newMethodName;
            } else if(child instanceof JavaHamlParser.MethodParametersContext || child instanceof JavaHamlParser.MethodParametersWithoutBracketsContext) {
                arguments.addAll(methodArgument((ParserRuleContext) child));
            }
        }

        return new MethodCallExpression(target, methodName, arguments);
    }

    private List<RubyExpression> methodArgument(ParserRuleContext context) {
        List<RubyExpression> arguments = new ArrayList<>();
        for (ParseTree child : context.children) {
            if(child instanceof JavaHamlParser.MethodParameterContext) {
                arguments.add(expression((ParserRuleContext)(child.getChild(0))));
            }
        }
        return arguments;
    }
}
