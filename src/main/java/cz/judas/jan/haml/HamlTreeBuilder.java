package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.TextNode;
import cz.judas.jan.haml.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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

    private Node tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        ImmutableList.Builder<RubyHash> attributeBuilder = ImmutableList.builder();
        ImmutableList.Builder<Node> childrenBuilder = ImmutableList.builder();
        String tagName = null;
        RubyExpression content = RubyString.EMPTY;

        for (ParseTree parseTree : htmlTagContext.children) {
            if (parseTree instanceof JavaHamlParser.TagNameContext) {
                tagName = parseTree.getChild(1).getText();
            } else if (parseTree instanceof JavaHamlParser.AttributeContext) {
                attributeBuilder.add(attributeHash(parseTree.getChild(0)));
            } else if (parseTree instanceof JavaHamlParser.TextContext) {
                content = new RubyString(parseTree.getText());
            } else if (parseTree instanceof JavaHamlParser.PlainTextContext) {
                content = new RubyString(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.EscapedTextContext) {
                content = new RubyString(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.RubyContentContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if (child instanceof JavaHamlParser.ExpressionContext) {
                        content = expression((ParserRuleContext) child);
                    }
                }
            } else if (parseTree instanceof JavaHamlParser.ChildTagsContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if (child instanceof JavaHamlParser.HtmlTagContext) {
                        childrenBuilder.add(tag((JavaHamlParser.HtmlTagContext) child));
                    }
                }
            }
        }

        List<RubyHash> attributes = attributeBuilder.build();
        List<Node> children = childrenBuilder.build();

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

    private RubyHash attributeHash(ParseTree node) {
        if (node instanceof JavaHamlParser.ClassAttributeContext) {
            return RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(node.getChild(1).getText()));
        } else if (node instanceof JavaHamlParser.IdAttributeContext) {
            return RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(node.getChild(1).getText()));
        } else if (node instanceof JavaHamlParser.AttributeHashContext) {
            return rubyAttributeHash((JavaHamlParser.AttributeHashContext) node);
        } else if (node instanceof JavaHamlParser.HtmlAttributesContext) {
            return htmlStyleAttributes((ParserRuleContext) node);
        }
        throw new IllegalArgumentException("Unknown attribute type " + node.getClass());
    }

    private RubyHash htmlStyleAttributes(ParserRuleContext parseTree) {
        ImmutableList.Builder<HashEntry> attributeBuilder = ImmutableList.builder();
        for (ParseTree child : parseTree.children) {
            if (child instanceof JavaHamlParser.HtmlAttributeEntryContext) {
                attributeBuilder.add(htmlStyleAttributeEntry((ParserRuleContext)child));
            }
        }
        return new RubyHash(attributeBuilder.build());
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
        return new HashEntry(new RubySymbol(key), value);
    }

    private RubyHash rubyAttributeHash(JavaHamlParser.AttributeHashContext parseTree) {
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
        return new RubyHash(hashEntryBuilder.build());
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

        return new HashEntry(new RubySymbol(key), value);
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
                return new RubySymbol(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.SingleQuotedStringContext) {
                return new RubyString(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.DoubleQuotedStringContext) {
                return new RubyString(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.FieldReferenceContext) {
                return new FieldReference(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.MethodCallContext) {
                return methodCall((JavaHamlParser.MethodCallContext) child);
            }
        }
        throw new IllegalArgumentException("Unknown expression type");
    }

    private MethodCall methodCall(JavaHamlParser.MethodCallContext context) {
        RubyExpression target = null;
        MethodCall result = null;

        for (ParseTree child : context.children) {
            if (child instanceof JavaHamlParser.FieldReferenceContext) {
                target = new FieldReference(child.getChild(1).getText());
            } else if (child instanceof JavaHamlParser.MethodNameContext) {
                String methodName = child.getText();

                if (result == null) {
                    result = new MethodCall(target, methodName);
                } else {
                    result = new MethodCall(result, methodName);
                }
            }
        }

        return result;
    }
}
