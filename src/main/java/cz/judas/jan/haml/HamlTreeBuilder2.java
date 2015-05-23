package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.antlr.JavaHamlLexer;
import cz.judas.jan.haml.antlr.JavaHamlParser;
import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.ruby.RubyString;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HamlTreeBuilder2 {
    public RootNode buildTreeFrom(String input) {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);

        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.HtmlTagContext htmlTagContext = parser.htmlTag();

        return new RootNode(
                Optional.empty(),
                ImmutableList.of(
                        new HtmlNode(
                                htmlTagContext.getChild(0).getChild(1).getText(),
                                Collections.emptyList(),
                                RubyString.EMPTY,
                                childrenOf(htmlTagContext)
                        )
                )
        );
    }

    private List<Node> childrenOf(ParseTree htmlTagContext) {
        List<Node> children = new ArrayList<>();
        for (int i = 1; i < htmlTagContext.getChildCount(); i++) {
            ParseTree child = htmlTagContext.getChild(i);
            if (child instanceof JavaHamlParser.HtmlTagContext) {
                children.add(new HtmlNode(
                        child.getChild(0).getChild(1).getText(),
                        Collections.emptyList(),
                        RubyString.EMPTY,
                        childrenOf(child)
                ));
            }
        }
        return children;
    }
}
