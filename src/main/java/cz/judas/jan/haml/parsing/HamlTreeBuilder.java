package cz.judas.jan.haml.parsing;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.parsing.antlr.JavaHamlLexer;
import cz.judas.jan.haml.parsing.antlr.JavaHamlParser;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HamlTreeBuilder {
    public RootNode buildTreeFrom(Reader input) throws IOException {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.DocumentContext documentContext = parser.document();

        return new RootNode(
                doctype(documentContext),
                Iterables.transform(documentContext.line(), this::line)
        );
    }

    private Optional<String> doctype(JavaHamlParser.DocumentContext document) {
        return Optional.ofNullable(document.doctype())
                .map(context -> context.actualDoctype().getText());
    }

    private HamlNode line(JavaHamlParser.LineContext context) {
        return Alternatives
                .either(context.htmlElement(), this::htmlElement)
                .or(context.escapedText(), text -> new TextNode(ConstantRubyExpression.string(text.hamlSpecialChar().getText() + text.text().getText())))
                .or(context.plainText(), this::plainText)
                .or(context.code(), code -> new CodeNode(codeNode(code)))
                .or(context.rubyContent(), ruby -> new TextNode(expression(ruby.expression())))
                .orException();
    }

    private TextNode plainText(JavaHamlParser.PlainTextContext context) {
        char firstChar = context.getText().charAt(0);
        if (firstChar == '\\' || firstChar == '=' || firstChar == '-' || firstChar == '%' || firstChar == '.' || firstChar == '#') {
            throw new IllegalArgumentException("Cannot parse " + context.getText());
        }
        return new TextNode(text(context.text()));
    }

    private RubyExpression text(JavaHamlParser.TextContext context) {
        ImmutableList.Builder<RubyExpression> builder = ImmutableList.builder();
        StringBuilder stringBuilder = new StringBuilder();
        for (JavaHamlParser.TextEntryContext entryContext : context.textEntry()) {
            if (entryContext.interpolatedExpression() != null) {
                if (stringBuilder.length() > 0) {
                    builder.add(ConstantRubyExpression.string(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                builder.add(expression(entryContext.interpolatedExpression().expression()));
            } else {
                stringBuilder.append(entryContext.getText());
            }
        }
        if (stringBuilder.length() > 0) {
            builder.add(ConstantRubyExpression.string(stringBuilder.toString()));
        }
        return CompoundStringExpression.from(builder.build());
    }

    private HamlNode htmlElement(JavaHamlParser.HtmlElementContext context) {
        String tagName = elementName(context.elementName());
        ImmutableList.Builder<RubyHashExpression> attributeBuilder = ImmutableList.builder();
        if(context.classAttribute() != null) {
            attributeBuilder.add(classAttribute(context.classAttribute()));
        }
        if(context.idAttribute() != null) {
            attributeBuilder.add(idAttribute(context.idAttribute()));
        }
        attributeBuilder.addAll(attributes(context.attribute()));

        return new HtmlNode(
                tagName,
                attributeBuilder.build(),
                elementContent(context.elementContent()),
                childTags(context)
        );
    }

    private ImmutableList<RubyHashExpression> attributes(List<JavaHamlParser.AttributeContext> context) {
        return FluentIterable.from(context).transform(this::attributeHash).toList();
    }

    private RubyExpression elementContent(JavaHamlParser.ElementContentContext context) {
        if (context != null) {
            return Alternatives
                    .either(context.rubyContent(), rubyContent -> expression(rubyContent.expression()))
                    .or(context.textContent(), textContent -> text(textContent.text()))
                    .orException();
        }
        return ConstantRubyExpression.EMPTY_STRING;
    }

    private List<HamlNode> childTags(JavaHamlParser.HtmlElementContext context) {
        if (context.childTags() != null) {
            return children(context.childTags());
        } else {
            return Collections.emptyList();
        }
    }

    private String elementName(JavaHamlParser.ElementNameContext context) {
        if (context == null) {
            return "div";
        } else {
            return context.getText().substring(1);
        }
    }

    private RubyExpression codeNode(JavaHamlParser.CodeContext codeContext) {
        RubyExpression expression = expression(codeContext.expression());
        JavaHamlParser.BlockContext blockContext = codeContext.block();
        if (blockContext != null) {
            PossibleMethodCall methodCallExpression = (PossibleMethodCall) expression;
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
        return FluentIterable.from(context.line())
                .transform(this::line)
                .toList();
    }

    private RubyHashExpression attributeHash(JavaHamlParser.AttributeContext context) {
        return Alternatives
                .either(context.classAttribute(), this::classAttribute)
                .or(context.idAttribute(), this::idAttribute)
                .or(context.attributeHash(), this::rubyAttributeHash)
                .or(context.htmlAttributes(), this::htmlStyleAttributes)
                .orException();
    }

    private RubyHashExpression classAttribute(JavaHamlParser.ClassAttributeContext classAttribute) {
        return RubyHashExpression.singleEntryHash(
                ConstantRubyExpression.symbol("class"),
                ConstantRubyExpression.string(classAttribute.getText().substring(1))
        );
    }

    private RubyHashExpression idAttribute(JavaHamlParser.IdAttributeContext idAttribute) {
        return RubyHashExpression.singleEntryHash(
                ConstantRubyExpression.symbol("id"),
                ConstantRubyExpression.string(idAttribute.getText().substring(1))
        );
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
                .or(context.localVariable(), this::localVariable)
                .orException();
    }

    private RubyExpression localVariable(JavaHamlParser.LocalVariableContext variable) {
        return new LocalVariableExpression(variable.getText());
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

    private RubyExpression fieldReference(JavaHamlParser.FieldReferenceContext context) {
        return new FieldReferenceExpression(context.WORD().getText());
    }

    private PossibleMethodCall methodCall(JavaHamlParser.MethodCallContext context) {
        RubyExpression target = methodTarget(context.methodTarget());
        PossibleMethodCall result = null;
        for (JavaHamlParser.SingleMethodCallContext singleMethodCallContext : context.singleMethodCall()) {
            Iterable<? extends RubyExpression> arguments = methodArguments(singleMethodCallContext.methodParameters());
            if(arguments == null) {
                result = new PropertyAccessExpression(target, singleMethodCallContext.methodName().getText());
            } else {
                result = new MethodCallExpression(
                        target,
                        singleMethodCallContext.methodName().getText(),
                        arguments,
                        RubyBlock.EMPTY
                );
            }
            target = result;
        }
        return result;
    }

    private Iterable<? extends RubyExpression> methodArguments(JavaHamlParser.MethodParametersContext context) {
        if (context != null) {
            return Alternatives
                    .either(context.methodParametersWithoutBrackets(), this::methodParametersWithoutBrackets)
                    .or(context.methodParametersWithBrackets(), params -> methodParametersWithoutBrackets(params.methodParametersWithoutBrackets()))
                    .or(context.emptyMethodParameters(), emptyParams -> Collections.emptyList())
                    .orException();
        }
        return null;
    }

    private Iterable<? extends RubyExpression> methodParametersWithoutBrackets(JavaHamlParser.MethodParametersWithoutBracketsContext context) {
        return Iterables.transform(context.expression(), this::expression);
    }

    private RubyExpression methodTarget(JavaHamlParser.MethodTargetContext context) {
        return Alternatives
                .either(context.fieldReference(), this::fieldReference)
                .or(context.localVariable(), this::localVariable)
                .orException();
    }
}
