package cz.judas.jan.haml.parsing;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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
        return Alternatives
                .either(context.realHtmlTag(), this::realHtmlTag)
                .or(context.escapedText(), text -> new TextNode(ConstantRubyExpression.string(text.hamlSpecialChar().getText() + text.text().getText())))
                .or(context.plainText(), text -> new TextNode(text(text.text())))
                .or(context.code(), code -> new CodeNode(codeNode(code)))
                .or(context.rubyContent(), ruby -> new TextNode(expression(ruby.expression())))
                .orException();
    }

    private RubyExpression text(JavaHamlParser.TextContext context) {
        // TODO check first char
        ImmutableList.Builder<RubyExpression> builder = ImmutableList.builder();
        StringBuilder stringBuilder = new StringBuilder();
        for (JavaHamlParser.TextEntryContext entryContext : context.textEntry()) {
            if (entryContext.interpolatedExpression() != null) {
                if(stringBuilder.length() > 0) {
                    builder.add(ConstantRubyExpression.string(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                builder.add(expression(entryContext.interpolatedExpression().expression()));
            } else {
                stringBuilder.append(entryContext.getText());
            }
        }
        if(stringBuilder.length() > 0) {
            builder.add(ConstantRubyExpression.string(stringBuilder.toString()));
        }
        return CompoundStringExpression.from(builder.build());
    }

    private HamlNode realHtmlTag(JavaHamlParser.RealHtmlTagContext context) {
        String tagName = tagName(context.tagName());
        List<RubyHashExpression> attributes = attributes(context.attribute());
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

    private ImmutableList<RubyHashExpression> attributes(List<JavaHamlParser.AttributeContext> context) {
        return FluentIterable.from(context).transform(this::attributeHash).toList();
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
            return methodCallExpression.withBlock(block(blockContext));
        } else {
            return expression;
        }
    }

    private BlockExpression block(JavaHamlParser.BlockContext context) {
        return new BlockExpression(
                children(context.childTags()),
                blockArguments(context.blockArguments())
        );
    }

    private Iterable<String> blockArguments(JavaHamlParser.BlockArgumentsContext context) {
        if (context == null) {
            return Collections.emptyList();
        } else {
            return Iterables.transform(context.localVariable(), ParseTree::getText);
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
        return Alternatives
                .either(parseTree.newStyleHashEntries(), this::newStyleHashEntries)
                .or(parseTree.oldStyleHashEntries(), this::oldStyleHashEntries)
                .orException();
    }

    private Iterable<HashEntry> newStyleHashEntries(JavaHamlParser.NewStyleHashEntriesContext context) {
        return Iterables.transform(context.newStyleHashEntry(), this::hashEntry);
    }

    private Iterable<HashEntry> oldStyleHashEntries(JavaHamlParser.OldStyleHashEntriesContext hashEntries) {
        return Iterables.transform(hashEntries.oldStyleHashEntry(), this::hashEntry);
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
        return Alternatives
                .either(context.symbol(), this::symbol)
                .or(context.singleQuotedString(), string -> ConstantRubyExpression.string(singleQuotedString(string)))
                .or(context.doubleQuotedString(), string -> ConstantRubyExpression.string(doubleQuotedString(string)))
                .or(context.fieldReference(), this::fieldReference)
                .or(context.methodCall(), this::methodCall)
                .or(context.intValue(), value -> ConstantRubyExpression.integer(Integer.parseInt(value.getText())))
                .or(context.localVariable(), variable -> new LocalVariableExpression(variable.getText()))
                .orException();
    }

    private RubyExpression symbol(JavaHamlParser.SymbolContext context) {
        return Alternatives
                .either(context.singleQuotedString(), string -> ConstantRubyExpression.symbol(singleQuotedString(string)))
                .or(context.WORD(), word -> ConstantRubyExpression.symbol(word.getText()))
                .orException();
    }

    private String doubleQuotedString(JavaHamlParser.DoubleQuotedStringContext context) {
        return nonEmptyDoubleQuotedString(context.nonEmptyDoubleQuotedString());
    }

    private String nonEmptyDoubleQuotedString(JavaHamlParser.NonEmptyDoubleQuotedStringContext context) {
        return context.doubleQuotedStringContent().getText();
    }

    private String singleQuotedString(JavaHamlParser.SingleQuotedStringContext context) {
        return context.singleQuotedStringContent().getText();
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
