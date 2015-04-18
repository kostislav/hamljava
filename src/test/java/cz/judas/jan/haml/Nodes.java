package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
                Collections.emptyMap(),
                RubyString.EMPTY,
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, Node... children) {
        return new HtmlNode(
                name,
                attributes,
                RubyString.EMPTY,
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, RubyExpression content) {
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
                Collections.emptyMap(),
                content,
                Collections.emptyList()
        );
    }

    public static TextNode textNode(RubyExpression content) {
        return new TextNode(content);
    }
}
