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

public class HamlTreeBuilder2 {
    public RootNode buildTreeFrom(String input) {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.DocumentContext documentContext = parser.document();

        return new RootNode(
                doctype(documentContext),
                FluentIterable.from(documentContext.children)
                        .filter(JavaHamlParser.HtmlTagContext.class)
                        .transform(HamlTreeBuilder2::tag)
                        .toList()
        );
    }

    private static Optional<String> doctype(ParseTree document) {
        ParseTree firstChild = document.getChild(0);
        if (firstChild instanceof JavaHamlParser.DoctypeContext) {
            return Optional.of(firstChild.getChild(2).getText());
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static Node tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        ImmutableList.Builder<RubyHash> attributeBuilder = ImmutableList.builder();
        ImmutableList.Builder<Node> childrenBuilder = ImmutableList.builder();
        String tagName = null;
        RubyExpression content = RubyString.EMPTY;

        for (ParseTree parseTree : htmlTagContext.children) {
            if (parseTree instanceof JavaHamlParser.TagNameContext) {
                tagName = parseTree.getChild(1).getText();
            } else if (parseTree instanceof JavaHamlParser.AttributeContext) {
                ParseTree child = parseTree.getChild(0);
                if (child instanceof JavaHamlParser.ClassAttributeContext) {
                    attributeBuilder.add(
                            RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(child.getChild(1).getText()))
                    );
                } else if (child instanceof JavaHamlParser.IdAttributeContext) {
                    attributeBuilder.add(
                            RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(child.getChild(1).getText()))
                    );
                } else if(child instanceof JavaHamlParser.AttributeHashContext) {
                    attributeBuilder.add(attributeHash((JavaHamlParser.AttributeHashContext) child));
                }
            } else if (parseTree instanceof JavaHamlParser.TextContext) {
                content = new RubyString(parseTree.getText());
            } else if (parseTree instanceof JavaHamlParser.PlainTextContext) {
                content = new RubyString(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.EscapedTextContext) {
                content = new RubyString(parseTree.getChild(1).getText());
            } else if(parseTree instanceof JavaHamlParser.RubyContentContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if(child instanceof JavaHamlParser.ExpressionContext) {
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

        if(tagName == null &&  attributes.isEmpty() && children.isEmpty()) {
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

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static RubyHash attributeHash(JavaHamlParser.AttributeHashContext parseTree) {
        ImmutableList.Builder<HashEntry> hashEntryBuilder = ImmutableList.builder();
        for (ParseTree child : parseTree.children) {
            if(child instanceof JavaHamlParser.NewStyleHashEntriesContext) {
                for (ParseTree grandChild : ((ParserRuleContext) child).children) {
                    if(grandChild instanceof JavaHamlParser.NewStyleHashEntryContext) {
                        hashEntryBuilder.add(hashEntry((JavaHamlParser.NewStyleHashEntryContext) grandChild));
                    }
                }
            } else if(child instanceof JavaHamlParser.OldStyleHashEntriesContext) {
                for (ParseTree grandChild : ((ParserRuleContext) child).children) {
                    if(grandChild instanceof JavaHamlParser.OldStyleHashEntryContext) {
                        hashEntryBuilder.add(hashEntry((JavaHamlParser.OldStyleHashEntryContext) grandChild));
                    }
                }
            }
        }
        return new RubyHash(hashEntryBuilder.build());
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static HashEntry hashEntry(JavaHamlParser.NewStyleHashEntryContext context) {
        String key = null;
        RubyExpression value = null;

        for (ParseTree child : context.children) {
            if(child instanceof JavaHamlParser.AttributeKeyContext) {
                key = child.getChild(0).getText();
            } else if(child instanceof JavaHamlParser.ExpressionContext) {
                value = expression((ParserRuleContext) child);
            }
        }

        return new HashEntry(new RubySymbol(key), value);
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static HashEntry hashEntry(JavaHamlParser.OldStyleHashEntryContext context) {
        RubyExpression key = null;
        RubyExpression value = null;

        for (ParseTree child : context.children) {
            if(child instanceof JavaHamlParser.KeyExpressionContext) {
                key = expression((ParserRuleContext) child.getChild(0));
            } else if(child instanceof JavaHamlParser.ValueExpressionContext) {
                value = expression((ParserRuleContext) child.getChild(0));
            }
        }

        return new HashEntry(key, value);
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    private static RubyExpression expression(ParserRuleContext context) {
        for (ParseTree child : context.children) {
            if(child instanceof JavaHamlParser.SymbolContext) {
                return new RubySymbol(child.getChild(1).getText());
            } else if(child instanceof JavaHamlParser.SingleQuotedStringContext) {
                return new RubyString(child.getChild(1).getText());
            } else if(child instanceof JavaHamlParser.FieldReferenceContext) {
                return new FieldReference(child.getChild(1).getText());
            }
        }
        throw new IllegalArgumentException("Unknown expression type");
    }
}
