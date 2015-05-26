package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HamlTreeBuilder {
    public RootNode buildTreeFrom(String input) {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.DocumentContext documentContext = parser.document();

        return new RootNode(
                doctype(documentContext),
                Iterables.transform(documentContext.htmlTag(), this::tag)
        );
    }

    private Optional<String> doctype(JavaHamlParser.DocumentContext document) {
        return Optional.ofNullable(document.doctype())
                .map(context -> context.actualDoctype().getText());
    }

    private HamlNode tag(JavaHamlParser.HtmlTagContext context) {
        return Alternatives
                .either(context.realHtmlTag(), this::realHtmlTag)
                .or(context.escapedText(), text -> new TextNode(ConstantRubyExpression.string(text.text().getText())))
                .or(context.plainText(), text -> new TextNode(ConstantRubyExpression.string(text.getText())))
                .or(context.code(), code -> new CodeNode(codeNode(code)))
                .or(context.rubyContent(), ruby -> new TextNode(expression(ruby.expression())))
                .orException();
    }

    private HamlNode realHtmlTag(JavaHamlParser.RealHtmlTagContext context) {
        String tagName = tagName(context.tagName());
        List<RubyHashExpression> attributes = FluentIterable.from(context.attribute())
                .transform(this::attributeHash)
                .toList();
        List<HamlNode> children = childTags(context);
        RubyExpression content = tagContent(context.tagContent());

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

    private RubyExpression tagContent(JavaHamlParser.TagContentContext context) {
        if (context != null) {
            if (context.rubyContent() != null) {
                return expression(context.rubyContent().expression());
            } else if (context.textContent() != null) {
                return ConstantRubyExpression.string(context.textContent().text().getText());
            }
        }
        return ConstantRubyExpression.EMPTY_STRING;
    }

    private List<HamlNode> childTags(JavaHamlParser.RealHtmlTagContext context) {
        if (context.childTags() != null) {
            return children(context.childTags());
        } else {
            return Collections.emptyList();
        }
    }

    private String tagName(JavaHamlParser.TagNameContext context) {
        if (context == null) {
            return null;
        } else {
            return context.WORD().getText();
        }
    }

    private RubyExpression codeNode(JavaHamlParser.CodeContext codeContext) {
        RubyExpression expression = expression(codeContext.expression());
        JavaHamlParser.BlockContext blockContext = codeContext.block();
        if (blockContext != null) {
            MethodCallExpression methodCallExpression = (MethodCallExpression) expression;
            return methodCallExpression.withBlock(
                    new BlockExpression(
                            children(blockContext.childTags())
                    )
            );
        } else {
            return expression;
        }
    }

    private List<HamlNode> children(JavaHamlParser.ChildTagsContext context) {
        return FluentIterable.from(context.htmlTag())
                .transform(this::tag)
                .toList();
    }

    private RubyHashExpression attributeHash(JavaHamlParser.AttributeContext context) {
        return Alternatives
                .either(context.classAttribute(), classAttribute -> RubyHashExpression.singleEntryHash(
                        ConstantRubyExpression.symbol("class"),
                        ConstantRubyExpression.string(classAttribute.WORD().getText())
                ))
                .or(context.idAttribute(), idAttribute -> RubyHashExpression.singleEntryHash(
                        ConstantRubyExpression.symbol("id"),
                        ConstantRubyExpression.string(idAttribute.WORD().getText())
                ))
                .or(context.attributeHash(), this::rubyAttributeHash)
                .or(context.htmlAttributes(), this::htmlStyleAttributes)
                .orException();
    }

    private RubyHashExpression htmlStyleAttributes(JavaHamlParser.HtmlAttributesContext context) {
        return new RubyHashExpression(
                Iterables.transform(
                        context.htmlAttributeEntry(),
                        this::htmlStyleAttributeEntry
                )
        );
    }

    private HashEntry htmlStyleAttributeEntry(JavaHamlParser.HtmlAttributeEntryContext context) {
        return new HashEntry(
                ConstantRubyExpression.symbol(context.htmlAttributeKey().getText()),
                expression(context.expression())
        );
    }

    private RubyHashExpression rubyAttributeHash(JavaHamlParser.AttributeHashContext parseTree) {
        return new RubyHashExpression(hashEntries(parseTree));
    }

    private Iterable<HashEntry> hashEntries(JavaHamlParser.AttributeHashContext parseTree) {
        if (parseTree.newStyleHashEntries() != null) {
            return Iterables.transform(
                    parseTree.newStyleHashEntries().newStyleHashEntry(),
                    this::hashEntry
            );
        } else {
            return Iterables.transform(
                    parseTree.oldStyleHashEntries().oldStyleHashEntry(),
                    this::hashEntry
            );
        }
    }

    private HashEntry hashEntry(JavaHamlParser.NewStyleHashEntryContext context) {
        return new HashEntry(
                ConstantRubyExpression.symbol(context.attributeKey().WORD().getText()),
                expression(context.expression())
        );
    }

    private HashEntry hashEntry(JavaHamlParser.OldStyleHashEntryContext context) {
        return new HashEntry(
                expression(context.keyExpression().expression()),
                expression(context.valueExpression().expression())
        );
    }

    private RubyExpression expression(JavaHamlParser.ExpressionContext context) {
        if (context.symbol() != null) {
            JavaHamlParser.SymbolContext symbol = context.symbol();
            if (symbol.singleQuotedString() != null) {
                return ConstantRubyExpression.symbol(symbol.singleQuotedString().singleQuotedStringContent().getText());
            } else {
                return ConstantRubyExpression.symbol(symbol.WORD().getText());
            }
        } else if (context.singleQuotedString() != null) {
            return ConstantRubyExpression.string(context.singleQuotedString().singleQuotedStringContent().getText());
        } else if (context.doubleQuotedString() != null) {
            return ConstantRubyExpression.string(context.doubleQuotedString().doubleQuotedStringContent().getText());
        } else if (context.fieldReference() != null) {
            return fieldReference(context.fieldReference());
        } else if (context.methodCall() != null) {
            return methodCall(context.methodCall());
        } else {
            return ConstantRubyExpression.integer(Integer.parseInt(context.intValue().getText()));
        }
    }

    private FieldReferenceExpression fieldReference(JavaHamlParser.FieldReferenceContext context) {
        return new FieldReferenceExpression(context.WORD().getText());
    }

    private MethodCallExpression methodCall(JavaHamlParser.MethodCallContext context) {
        RubyExpression target = fieldReference(context.fieldReference());
        MethodCallExpression result = null;
        for (JavaHamlParser.SingleMethodCallContext singleMethodCallContext : context.singleMethodCall()) {
            result = new MethodCallExpression(
                    target,
                    singleMethodCallContext.methodName().getText(),
                    methodArguments(singleMethodCallContext.methodParameters())
            );
            target = result;
        }
        return result;
    }

    private Iterable<? extends RubyExpression> methodArguments(JavaHamlParser.MethodParametersContext context) {
        if (context != null) {
            if (context.methodParametersWithoutBrackets() != null) {
                return methodParametersWithoutBrackets(context.methodParametersWithoutBrackets());
            } else if (context.methodParametersWithBrackets() != null) {
                return methodParametersWithoutBrackets(context.methodParametersWithBrackets().methodParametersWithoutBrackets());
            }
        }
        return Collections.emptyList();
    }

    private Iterable<? extends RubyExpression> methodParametersWithoutBrackets(JavaHamlParser.MethodParametersWithoutBracketsContext context) {
        return Iterables.transform(context.expression(), this::expression);
    }

    private static class Alternatives<T> {
        private final List<Alternative<?, T>> alternatives = new ArrayList<>();

        public static <I, O> Alternatives<O> either(I value, Function<I, O> transform) {
            Alternatives<O> alternatives = new Alternatives<>();
            return alternatives.or(value, transform);
        }

        public <I> Alternatives<T> or(I value, Function<I, T> transform) {
            alternatives.add(new Alternative<>(value, transform));
            return this;
        }

        public T orDefault(T defaultValue) {
            for (Alternative<?, T> alternative : alternatives) {
                if (alternative.value != null) {
                    return alternative.value();
                }
            }
            return defaultValue;
        }

        public T orException() {
            for (Alternative<?, T> alternative : alternatives) {
                if (alternative.value != null) {
                    return alternative.value();
                }
            }
            throw new IllegalArgumentException("No alternative found");
        }

        private static class Alternative<I, O> {
            private final I value;
            private final Function<I, O> transform;

            private Alternative(I value, Function<I, O> transform) {
                this.value = value;
                this.transform = transform;
            }

            public O value() {
                return transform.apply(value);
            }
        }
    }
}
