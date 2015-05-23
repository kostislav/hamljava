package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.ruby.RubyString;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collections;
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

    private static HtmlNode tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        return new HtmlNode(
                htmlTagContext.getChild(0).getChild(1).getText(),
                Collections.emptyList(),
                FluentIterable.from(htmlTagContext.children)
                        .filter(JavaHamlParser.TextContext.class)
                        .first()
                        .transform(ParserRuleContext::getText)
                        .transform(RubyString::new)
                        .or(RubyString.EMPTY),
                childrenOf(htmlTagContext)
        );
    }

    private static List<? extends Node> childrenOf(JavaHamlParser.HtmlTagContext htmlTagContext) {
        return FluentIterable.from(htmlTagContext.children)
                .filter(JavaHamlParser.ChildTagsContext.class)
                .transformAndConcat(HamlTreeBuilder2::children)
                .filter(JavaHamlParser.HtmlTagContext.class)
                .transform(HamlTreeBuilder2::tag)
                .toList();
    }

    private static List<ParseTree> children(ParserRuleContext parseTree) {
        return parseTree.children;
    }
}
