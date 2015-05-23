package cz.judas.jan.haml;

import com.google.common.collect.FluentIterable;
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

import java.util.*;

public class HamlTreeBuilder2 {
    public RootNode buildTreeFrom(String input) {
        JavaHamlLexer lexer = new JavaHamlLexer(new ANTLRInputStream(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);

        JavaHamlParser parser = new JavaHamlParser(tokenStream);

        JavaHamlParser.HtmlTagContext htmlTagContext = parser.htmlTag();

        return new RootNode(
                Optional.empty(),
                ImmutableList.of(tag(htmlTagContext))
        );
    }

    private static HtmlNode tag(JavaHamlParser.HtmlTagContext htmlTagContext) {
        return new HtmlNode(
                htmlTagContext.getChild(0).getChild(1).getText(),
                Collections.emptyList(),
                RubyString.EMPTY,
                childrenOf(htmlTagContext)
        );
    }

    private static List<? extends Node> childrenOf(JavaHamlParser.HtmlTagContext htmlTagContext) {
        return FluentIterable.from(new ParseTreeChildren(htmlTagContext))
                .filter(JavaHamlParser.ChildTagsContext.class)
                .transformAndConcat(ParseTreeChildren::new)
                .filter(JavaHamlParser.HtmlTagContext.class)
                .transform(HamlTreeBuilder2::tag)
                .toList();
    }

    private static class ParseTreeChildren implements Iterable<ParseTree> {
        private final ParseTree parent;

        private ParseTreeChildren(ParseTree parent) {
            this.parent = parent;
        }

        @Override
        public Iterator<ParseTree> iterator() {
            return new ParseTreeChildIterator(parent);
        }

        @SuppressWarnings("InnerClassTooDeeplyNested")
        private static class ParseTreeChildIterator implements Iterator<ParseTree> {
            private final ParseTree parent;
            private int i = 0;

            private ParseTreeChildIterator(ParseTree parent) {
                this.parent = parent;
            }

            @Override
            public boolean hasNext() {
                return i < parent.getChildCount();
            }

            @Override
            public ParseTree next() {
                i += 1;
                return parent.getChild(i - 1);
            }
        }
    }
}
