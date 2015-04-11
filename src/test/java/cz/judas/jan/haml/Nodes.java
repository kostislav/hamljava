package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.TextNode;

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
                "",
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<String, String> attributes, Node... children) {
        return new HtmlNode(
                name,
                attributes,
                "",
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<String, String> attributes, String content) {
        return new HtmlNode(
                name,
                attributes,
                content,
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, String content) {
        return new HtmlNode(
                name,
                Collections.emptyMap(),
                content,
                Collections.emptyList()
        );
    }

    public static TextNode textNode(String content) {
        return new TextNode(content);
    }
}
