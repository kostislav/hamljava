package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
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
                Iterables.transform(documentContext.htmlTag(), this::tag)
        );
    }

    private Optional<String> doctype(JavaHamlParser.DocumentContext document) {
        return Optional.ofNullable(document.doctype())
                .map(context -> context.actualDoctype().getText());
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
                attributeBuilder.add(attributeHash(((JavaHamlParser.AttributeContext) parseTree)));
            } else if (parseTree instanceof JavaHamlParser.PlainTextContext) {
                content = ConstantRubyExpression.string(parseTree.getText());
            } else if (parseTree instanceof JavaHamlParser.TextContentContext) {
                content = ConstantRubyExpression.string(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.EscapedTextContext) {
                content = ConstantRubyExpression.string(parseTree.getChild(1).getText());
            } else if (parseTree instanceof JavaHamlParser.CodeContext) {
                return new CodeNode(codeNode((JavaHamlParser.CodeContext) parseTree));
            } else if (parseTree instanceof JavaHamlParser.RubyContentContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if (child instanceof JavaHamlParser.ExpressionContext) {
                        content = expression((JavaHamlParser.ExpressionContext) child);
                    }
                }
            } else if (parseTree instanceof JavaHamlParser.ChildTagsContext) {
                childrenBuilder.addAll(children((JavaHamlParser.ChildTagsContext) parseTree));
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
            return new FieldReferenceExpression(context.fieldReference().WORD().getText());
        } else if (context.methodCall() != null) {
            return methodCall(context.methodCall());
        } else {
            return ConstantRubyExpression.integer(Integer.parseInt(context.intValue().getText()));
        }
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
            } else if (child instanceof JavaHamlParser.MethodParametersContext) {
                arguments.addAll(methodArgument(((JavaHamlParser.MethodParametersContext) child).methodParametersWithoutBrackets()));
            } else if (child instanceof JavaHamlParser.MethodParametersWithoutBracketsContext) {
                arguments.addAll(methodArgument((JavaHamlParser.MethodParametersWithoutBracketsContext) child));
            }
        }

        return new MethodCallExpression(target, methodName, arguments);
    }

    private List<RubyExpression> methodArgument(JavaHamlParser.MethodParametersWithoutBracketsContext context) {
        return FluentIterable.from(context.methodParameter())
                .transform(param -> expression(param.expression()))
                .toList();
    }
}
