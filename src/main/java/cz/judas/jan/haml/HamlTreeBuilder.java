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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        if (context.realHtmlTag() != null) {
            return realHtmlTag(context.realHtmlTag());
        } else if (context.escapedText() != null) {
            return new TextNode(ConstantRubyExpression.string(context.escapedText().text().getText()));
        } else if (context.plainText() != null) {
            return new TextNode(ConstantRubyExpression.string(context.plainText().getText()));
        } else if (context.code() != null) {
            return new CodeNode(codeNode(context.code()));
        } else if (context.rubyContent() != null) {
            return new TextNode(expression(context.rubyContent().expression()));
        }
        throw new IllegalStateException("No content found");
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
        if(context != null) {
            if(context.rubyContent() != null) {
                return expression(context.rubyContent().expression());
            } else if(context.textContent() != null) {
                return ConstantRubyExpression.string(context.textContent().text().getText());
            }
        }
        return ConstantRubyExpression.EMPTY_STRING;
    }

    private List<HamlNode> childTags(JavaHamlParser.RealHtmlTagContext context) {
        if(context.childTags() != null) {
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
        if (context.classAttribute() != null) {
            return RubyHashExpression.singleEntryHash(
                    ConstantRubyExpression.symbol("class"),
                    ConstantRubyExpression.string(context.classAttribute().WORD().getText())
            );
        } else if (context.idAttribute() != null) {
            return RubyHashExpression.singleEntryHash(
                    ConstantRubyExpression.symbol("id"),
                    ConstantRubyExpression.string(context.idAttribute().WORD().getText())
            );
        } else if (context.attributeHash() != null) {
            return rubyAttributeHash(context.attributeHash());
        } else {
            return htmlStyleAttributes(context.htmlAttributes());
        }
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
}
