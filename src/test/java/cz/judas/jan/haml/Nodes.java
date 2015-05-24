package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.ruby.HashEntry;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHashExpression;
import cz.judas.jan.haml.tree.ruby.RubyStringExpression;

import java.util.*;

@SuppressWarnings("UtilityClass")
public class Nodes {
    public static RootNode root(HamlNode... children) {
        return new RootNode(Optional.empty(), Arrays.asList(children));
    }

    public static RootNode root(String doctype, HamlNode... children) {
        return new RootNode(Optional.of(doctype), Arrays.asList(children));
    }

    public static HtmlNode node(String name) {
        return new HtmlNode(
                name,
                Collections.emptyList(),
                RubyStringExpression.EMPTY,
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, List<? extends HamlNode> children) {
        return new HtmlNode(
                name,
                Collections.emptyList(),
                RubyStringExpression.EMPTY,
                children
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, HamlNode... children) {
        return new HtmlNode(
                name,
                mapToHash(attributes),
                RubyStringExpression.EMPTY,
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

    public static HtmlNode node(String name, List<RubyHashExpression> attributes, RubyExpression content) {
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

    private static List<RubyHashExpression> mapToHash(Map<RubyExpression, RubyExpression> map) {
        ImmutableList.Builder<HashEntry> builder = ImmutableList.builder();
        for (Map.Entry<RubyExpression, RubyExpression> entry : map.entrySet()) {
            builder.add(new HashEntry(entry.getKey(), entry.getValue()));
        }
        return ImmutableList.of(new RubyHashExpression(builder.build()));
    }
}
