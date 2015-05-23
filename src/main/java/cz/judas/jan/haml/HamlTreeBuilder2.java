package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import cz.judas.jan.haml.tree.ruby.RubyString;
import cz.judas.jan.haml.tree.ruby.RubySymbol;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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

    private static HtmlNode tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        ImmutableList.Builder<RubyHash> attributeBuilder = ImmutableList.builder();
        ImmutableList.Builder<Node> children = ImmutableList.builder();
        RubyExpression content = RubyString.EMPTY;

        for (ParseTree parseTree : htmlTagContext.children) {
            if(parseTree instanceof JavaHamlParser.ClassAttributeContext) {
                attributeBuilder.add(
                        RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(parseTree.getChild(1).getText()))
                );
            } else if(parseTree instanceof JavaHamlParser.TextContext) {
                content = new RubyString(parseTree.getText());
            } else if(parseTree instanceof JavaHamlParser.ChildTagsContext) {
                for (ParseTree child : ((ParserRuleContext) parseTree).children) {
                    if(child instanceof JavaHamlParser.HtmlTagContext) {
                        children.add(tag((JavaHamlParser.HtmlTagContext) child));
                    }
                }
            }
        }

        return new HtmlNode(
                htmlTagContext.getChild(0).getChild(1).getText(),
                attributeBuilder.build(),
                content,
                children.build()
        );
    }
}
