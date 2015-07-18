package cz.judas.jan.hamljava.testutil;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.template.tree.*;
import cz.judas.jan.hamljava.template.tree.ruby.HashEntry;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;
import cz.judas.jan.hamljava.template.tree.ruby.RubyHashExpression;

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
                EmptyNode.INSTANCE,
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, List<? extends HamlNode> children) {
        return new HtmlNode(
                name,
                Collections.emptyList(),
                EmptyNode.INSTANCE,
                children
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, HamlNode... children) {
        return new HtmlNode(
                name,
                mapToHash(attributes),
                EmptyNode.INSTANCE,
                Arrays.asList(children)
        );
    }

    public static HtmlNode node(String name, Map<RubyExpression, RubyExpression> attributes, RubyExpression content) {
        return new HtmlNode(
                name,
                mapToHash(attributes),
                new TextNode(content),
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, List<RubyHashExpression> attributes, RubyExpression content) {
        return new HtmlNode(
                name,
                attributes,
                new TextNode(content),
                Collections.emptyList()
        );
    }

    public static HtmlNode node(String name, List<RubyHashExpression> attributes, HamlNode content) {
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
                new TextNode(content),
                Collections.emptyList()
        );
    }

    public static TextNode textNode(RubyExpression content) {
        return new TextNode(content);
    }

    public static CodeNode codeNode(RubyExpression code) {
        return new CodeNode(code);
    }

    private static List<RubyHashExpression> mapToHash(Map<RubyExpression, RubyExpression> map) {
        ImmutableList.Builder<HashEntry> builder = ImmutableList.builder();
        for (Map.Entry<RubyExpression, RubyExpression> entry : map.entrySet()) {
            builder.add(new HashEntry(entry.getKey(), entry.getValue()));
        }
        return ImmutableList.of(new RubyHashExpression(builder.build()));
    }
}
