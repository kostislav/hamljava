package cz.judas.jan.hamljava.parsing;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cz.judas.jan.hamljava.parsing.antlr.JavaHamlLexer;
import cz.judas.jan.hamljava.parsing.antlr.JavaHamlParser;
import cz.judas.jan.hamljava.template.tree.*;
import cz.judas.jan.hamljava.template.tree.ruby.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HamlTreeBuilder {
    private final FunctionalNodeBuilder functionalNodeBuilder;

    public HamlTreeBuilder(FunctionalNodeBuilder functionalNodeBuilder) {
        this.functionalNodeBuilder = functionalNodeBuilder;
    }

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
                .or(context.escapedText(), this::escapedText)
                .or(context.plainText(), this::plainText)
                .or(context.code(), this::code)
                .or(context.rubyContent(), this::rubyContent)
                .orException();
    }

    private CodeNode code(JavaHamlParser.CodeContext context) {
        return new CodeNode(statement(context.statement()));
    }

    private TextNode escapedText(JavaHamlParser.EscapedTextContext context) {
        return new TextNode(new ConstantRubyExpression(context.hamlSpecialChar().getText() + context.text().getText()));
    }

    private TextNode plainText(JavaHamlParser.PlainTextContext context) {
        char firstChar = context.getText().charAt(0);
        if (firstChar == '\\' || firstChar == '=' || firstChar == '-' || firstChar == '%' || firstChar == '.' || firstChar == '#') {
            throw new IllegalArgumentException("Cannot parse " + context.getText());
        }
        return new TextNode(text(context.text()));
    }

    private RubyExpression text(JavaHamlParser.TextContext context) {
        return stringWithInterpolation(Iterables.transform(
                context.textEntry(),
                TextEntry::new
        ));
    }

    private HamlNode htmlElement(JavaHamlParser.HtmlElementContext context) {
        TagDefinition tagDefinition = elementDefinition(context.elementDefinition());
        ContentDefinition contentDefinition = contentDefinition(context.elementContent());

        return new HtmlNode(
                tagDefinition.getTagName(),
                tagDefinition.getAttributes(),
                contentDefinition.getDirectContent(),
                contentDefinition.getChildren()
        );
    }

    private TagDefinition elementDefinition(JavaHamlParser.ElementDefinitionContext context) {
        return new TagDefinition(
                elementName(context.elementName()),
                attributes(context)
        );
    }

    private ContentDefinition contentDefinition(JavaHamlParser.ElementContentContext context) {
        return new ContentDefinition(
                Alternatives
                        .either(context.textContent(), this::textContent)
                        .or(context.rubyContent(), this::rubyContent)
                        .orDefault(EmptyNode.INSTANCE),
                childTags(context)
        );
    }

    private List<RubyHashExpression> attributes(JavaHamlParser.ElementDefinitionContext context) {
        ImmutableList.Builder<RubyHashExpression> attributeBuilder = ImmutableList.builder();
        if (context.classAttribute() != null) {
            attributeBuilder.add(classAttribute(context.classAttribute()));
        }
        if (context.idAttribute() != null) {
            attributeBuilder.add(idAttribute(context.idAttribute()));
        }
        attributeBuilder.addAll(FluentIterable.from(context.shortAttribute()).transform(this::shortAttribute));
        attributeBuilder.addAll(FluentIterable.from(context.longAttribute()).transform(this::longAttribute));
        return attributeBuilder.build();
    }

    private HamlNode textContent(JavaHamlParser.TextContentContext context) {
        return new TextNode(text(context.text()));
    }

    private HamlNode rubyContent(JavaHamlParser.RubyContentContext context) {
        return Alternatives
                .either(context.regularRubyContent(), this::regularRubyContent)
                .or(context.unescapedRubyContent(), this::unescapedRubyContent)
                .orException();
    }

    private UnescapedNode unescapedRubyContent(JavaHamlParser.UnescapedRubyContentContext context) {
        return new UnescapedNode(statement(context.regularRubyContent().statement()));
    }

    private HamlNode regularRubyContent(JavaHamlParser.RegularRubyContentContext context) {
        return new TextNode(statement(context.statement()));
    }

    private List<HamlNode> childTags(JavaHamlParser.ElementContentContext context) {
        return Alternatives
                .either(context.childTags(), this::children)
                .orDefault(Collections.emptyList());
    }

    private String elementName(JavaHamlParser.ElementNameContext context) {
        if (context == null) {
            return "div";
        } else {
            return context.getText().substring(1);
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

    private RubyHashExpression shortAttribute(JavaHamlParser.ShortAttributeContext context) {
        return Alternatives
                .either(context.classAttribute(), this::classAttribute)
                .or(context.idAttribute(), this::idAttribute)
                .orException();
    }

    private RubyHashExpression longAttribute(JavaHamlParser.LongAttributeContext context) {
        return Alternatives
                .either(context.attributeHash(), this::rubyAttributeHash)
                .or(context.htmlAttributes(), this::htmlStyleAttributes)
                .orException();
    }

    private RubyHashExpression classAttribute(JavaHamlParser.ClassAttributeContext context) {
        return RubyHashExpression.singleEntryHash(
                ConstantRubyExpression.symbol("class"),
                new ConstantRubyExpression(context.getText().substring(1))
        );
    }

    private RubyHashExpression idAttribute(JavaHamlParser.IdAttributeContext context) {
        return RubyHashExpression.singleEntryHash(
                ConstantRubyExpression.symbol("id"),
                new ConstantRubyExpression(context.getText().substring(1))
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
        return Alternatives
                .either(context.keyValueHtmlAttributeEntry(), this::keyValueHtmlAttributeEntry)
                .or(context.booleanHtmlAttributeEntry(), this::booleanHtmlAttributeEntry)
                .orException();
    }

    private HashEntry keyValueHtmlAttributeEntry(JavaHamlParser.KeyValueHtmlAttributeEntryContext context) {
        return new HashEntry(
                ConstantRubyExpression.symbol(context.htmlAttributeKey().getText()),
                expression(context.expression())
        );
    }

    private HashEntry booleanHtmlAttributeEntry(JavaHamlParser.BooleanHtmlAttributeEntryContext context) {
        return new HashEntry(
                ConstantRubyExpression.symbol(context.htmlAttributeKey().getText()),
                ConstantRubyExpression.TRUE
        );
    }

    private RubyHashExpression rubyAttributeHash(JavaHamlParser.AttributeHashContext context) {
        return new RubyHashExpression(hashEntries(context));
    }

    private Iterable<HashEntry> hashEntries(JavaHamlParser.AttributeHashContext context) {
        return Alternatives
                .either(context.newStyleHashEntries(), this::newStyleHashEntries)
                .or(context.oldStyleHashEntries(), this::oldStyleHashEntries)
                .orException();
    }

    private Iterable<HashEntry> newStyleHashEntries(JavaHamlParser.NewStyleHashEntriesContext context) {
        return Iterables.transform(context.newStyleHashEntry(), this::hashEntry);
    }

    private Iterable<HashEntry> oldStyleHashEntries(JavaHamlParser.OldStyleHashEntriesContext context) {
        return Iterables.transform(context.oldStyleHashEntry(), this::hashEntry);
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

    private RubyExpression statement(JavaHamlParser.StatementContext context) {
        return Alternatives
                .either(context.expression(), this::expression)
                .or(context.methodWithBlock(), this::methodWithBlock)
                .or(context.functionWithBlock(), this::functionCallWithBlock)
                .or(context.ifStatement(), this::ifStatement)
                .orException();
    }

    private RubyExpression functionCallWithBlock(JavaHamlParser.FunctionWithBlockContext context) {
        return functionCall(context.functionCall()).withBlock(block(context.block())).materialize();
    }


    private RubyExpression methodWithBlock(JavaHamlParser.MethodWithBlockContext context) {
        return methodCall(context.methodCall()).withBlock(block(context.block())).materialize();
    }

    private RubyExpression expression(JavaHamlParser.ExpressionContext context) {
        return Alternatives
                .either(context.symbol(), this::symbol)
                .or(context.singleQuotedString(), string -> new ConstantRubyExpression(singleQuotedString(string)))
                .or(context.doubleQuotedString(), this::doubleQuotedString)
                .or(context.fieldReference(), this::fieldReference)
                .or(context.methodCall(), this::methodCallWithoutBlock)
                .or(context.intValue(), value -> new ConstantRubyExpression(Integer.parseInt(value.getText())))
                .or(context.localVariable(), this::localVariable)
                .or(context.functionCall(), this::functionCallWithoutBlock)
                .or(context.negatedExpression(), this::negatedExpression)
                .orException();
    }

    private RubyExpression negatedExpression(JavaHamlParser.NegatedExpressionContext context) {
        return new NegationExpression(expression(context.expression()));
    }

    private RubyExpression ifStatement(JavaHamlParser.IfStatementContext context) {
        return new ConditionalExpression(
                expression(context.expression()),
                children(context.childTags())
        );
    }

    private RubyExpression localVariable(JavaHamlParser.LocalVariableContext context) {
        return functionalNodeBuilder.localVariable(context.getText()).materialize();
    }

    private RubyExpression symbol(JavaHamlParser.SymbolContext context) {
        return Alternatives
                .either(context.singleQuotedString(), string -> ConstantRubyExpression.symbol(singleQuotedString(string)))
                .or(context.WORD(), word -> ConstantRubyExpression.symbol(word.getText()))
                .orException();
    }

    private RubyExpression doubleQuotedString(JavaHamlParser.DoubleQuotedStringContext context) {
        return Alternatives
                .either(context.emptyDoubleQuotedString(), this::emptyDoubleQuotedString)
                .or(context.nonEmptyDoubleQuotedString(), this::nonEmptyDoubleQuotedString)
                .orException();
    }

    @SuppressWarnings("UnusedParameters")
    private RubyExpression emptyDoubleQuotedString(JavaHamlParser.EmptyDoubleQuotedStringContext context) {
        return ConstantRubyExpression.EMPTY_STRING;
    }

    private RubyExpression nonEmptyDoubleQuotedString(JavaHamlParser.NonEmptyDoubleQuotedStringContext context) {
        return stringWithInterpolation(Iterables.transform(
                context.doubleQuotedStringContent().doubleQuotedStringElement(),
                DoubleQuotedStringElement::new
        ));
    }

    private RubyExpression stringWithInterpolation(Iterable<? extends WithInterpolatedString> context) {
        ImmutableList.Builder<RubyExpression> builder = ImmutableList.builder();
        StringBuilder stringBuilder = new StringBuilder();
        for (WithInterpolatedString entryContext : context) {
            if (entryContext.interpolatedExpression() != null) {
                if (stringBuilder.length() > 0) {
                    builder.add(new ConstantRubyExpression(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                builder.add(expression(entryContext.interpolatedExpression().expression()));
            } else {
                stringBuilder.append(entryContext.getText());
            }
        }
        if (stringBuilder.length() > 0) {
            builder.add(new ConstantRubyExpression(stringBuilder.toString()));
        }
        return CompoundStringExpression.from(builder.build());
    }

    private String singleQuotedString(JavaHamlParser.SingleQuotedStringContext context) {
        return context.singleQuotedStringContent().getText();
    }

    private RubyExpression fieldReference(JavaHamlParser.FieldReferenceContext context) {
        return new FieldReferenceExpression(context.WORD().getText());
    }

    private RubyExpression methodCallWithoutBlock(JavaHamlParser.MethodCallContext context) {
        return methodCall(context).materialize();
    }

    private ParsedFunctionOrMethodCall methodCall(JavaHamlParser.MethodCallContext context) {
        RubyExpression target = methodTarget(context.methodTarget());
        return functionOrMethodCall(
                context.singleMethodCall(),
                context.chainedMethodCalls(),
                (name) -> functionalNodeBuilder.propertyAccess(target, name),
                (name, arguments) -> functionalNodeBuilder.methodCall(target, name, arguments)
        );
    }

    private RubyExpression functionCallWithoutBlock(JavaHamlParser.FunctionCallContext context) {
        return functionCall(context).materialize();
    }

    private ParsedFunctionOrMethodCall functionCall(JavaHamlParser.FunctionCallContext context) {
        return functionOrMethodCall(
                context.singleMethodCall(),
                context.chainedMethodCalls(),
                functionalNodeBuilder::localVariable,
                functionalNodeBuilder::functionCall
        );
    }

    private ParsedFunctionOrMethodCall functionOrMethodCall(
            JavaHamlParser.SingleMethodCallContext singleMethodCallContext,
            JavaHamlParser.ChainedMethodCallsContext chainedMethodCallsContext,
            Function<String, ParsedFunctionOrMethodCall> nonFunctionCreator,
            BiFunction<String, Iterable<? extends RubyExpression>, ParsedFunctionOrMethodCall> functionCreator
    ) {
        ParsedFunctionOrMethodCall result;
        Iterable<? extends RubyExpression> arguments = methodArguments(singleMethodCallContext.methodParameters());
        if (arguments == null) {
            result = nonFunctionCreator.apply(singleMethodCallContext.methodName().getText());
        } else {
            result = functionCreator.apply(
                    singleMethodCallContext.methodName().getText(),
                    arguments
            );
        }
        return chainedMethodCalls(result, chainedMethodCallsContext);
    }

    private ParsedFunctionOrMethodCall chainedMethodCalls(ParsedFunctionOrMethodCall target, JavaHamlParser.ChainedMethodCallsContext context) {
        ParsedFunctionOrMethodCall result = target;
        for (JavaHamlParser.SingleMethodCallContext singleMethodCallContext : context.singleMethodCall()) {
            Iterable<? extends RubyExpression> arguments = methodArguments(singleMethodCallContext.methodParameters());
            if (arguments == null) {
                result = functionalNodeBuilder.propertyAccess(result.materialize(), singleMethodCallContext.methodName().getText());
            } else {
                result = functionalNodeBuilder.methodCall(
                        target.materialize(),
                        singleMethodCallContext.methodName().getText(),
                        arguments
                );
                target = result;
            }
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

    private interface WithInterpolatedString {
        JavaHamlParser.InterpolatedExpressionContext interpolatedExpression();

        String getText();
    }

    private static class DoubleQuotedStringElement implements WithInterpolatedString {
        private final JavaHamlParser.DoubleQuotedStringElementContext context;

        private DoubleQuotedStringElement(JavaHamlParser.DoubleQuotedStringElementContext context) {
            this.context = context;
        }

        @Override
        public JavaHamlParser.InterpolatedExpressionContext interpolatedExpression() {
            return context.interpolatedExpression();
        }

        @Override
        public String getText() {
            return context.getText();
        }
    }

    private static class TextEntry implements WithInterpolatedString {
        private final JavaHamlParser.TextEntryContext context;

        private TextEntry(JavaHamlParser.TextEntryContext context) {
            this.context = context;
        }

        @Override
        public JavaHamlParser.InterpolatedExpressionContext interpolatedExpression() {
            return context.interpolatedExpression();
        }

        @Override
        public String getText() {
            return context.getText();
        }
    }

    private static class TagDefinition {
        private final String tagName;
        private final List<RubyHashExpression> attributes;

        private TagDefinition(String tagName, List<RubyHashExpression> attributes) {
            this.tagName = tagName;
            this.attributes = ImmutableList.copyOf(attributes);
        }

        private String getTagName() {
            return tagName;
        }

        private List<RubyHashExpression> getAttributes() {
            return ImmutableList.copyOf(attributes);
        }
    }

    private static class ContentDefinition {
        private final HamlNode directContent;
        private final List<HamlNode> children;

        private ContentDefinition(HamlNode directContent, List<HamlNode> children) {
            this.directContent = directContent;
            this.children = ImmutableList.copyOf(children);
        }

        private HamlNode getDirectContent() {
            return directContent;
        }

        private List<HamlNode> getChildren() {
            return ImmutableList.copyOf(children);
        }
    }
}
