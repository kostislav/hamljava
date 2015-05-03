package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.HashEntry;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import cz.judas.jan.haml.tree.ruby.RubyString;

import java.util.*;

@SuppressWarnings("UtilityClass")
public class Nodes {
    public static RootNode root(Node... children) {
        return new RootNode(Optional.empty(), Arrays.asList(children));
    }

    public static RootNode root(String doctype, Node... children) {
        return new RootNode(Optional.of(doctype), Arrays.asList(children));
    }

    public static HtmlNode node(String name, Node... children) {
        return new HtmlNode(
                name,
                Collections.emptyList(),
                RubyString.EMPTY,
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, Node... children) {
        return new HtmlNode(
                name,
                mapToHash(attributes),
                RubyString.EMPTY,
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, RubyExpression content) {
        return new HtmlNode(
                name,
                mapToHash(attributes),
                content,
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, List<RubyHash> attributes, RubyExpression content) {
        return new HtmlNode(
                name,
                attributes,
                content,
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, RubyExpression content) {
        return new HtmlNode(
                name,
                Collections.emptyList(),
                content,
                Collections.emptyList()
        );
    }

    public static TextNode textNode(RubyExpression content) {
        return new TextNode(content);
    }

    private static List<RubyHash> mapToHash(Map<RubyExpression, RubyExpression> map) {
        ImmutableList.Builder<HashEntry> builder = ImmutableList.builder();
        for (Map.Entry<RubyExpression, RubyExpression> entry : map.entrySet()) {
            builder.add(new HashEntry(entry.getKey(), entry.getValue()));
        }
        return ImmutableList.of(new RubyHash(builder.build()));
    }
}
