package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class HtmlNode implements Node {
    private final String tagName;
    private final Map<String, RubyValue> attributes;
    private final String textContent;
    private final List<Node> children;

    public HtmlNode(String tagName, Map<String, RubyValue> attributes, String textContent, Iterable<? extends Node> children) {
        this.tagName = tagName;
        this.attributes = ImmutableMap.copyOf(attributes);
        this.textContent = textContent;
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder) {
        stringBuilder
                .append('<').append(tagName);

        for (Map.Entry<String, RubyValue> entry : attributes.entrySet()) {
            stringBuilder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue().evaluate()).append('"');
        }
        stringBuilder.append('>');
        stringBuilder.append(textContent);
        for (Node child : children) {
            child.appendTo(stringBuilder);
        }
        stringBuilder.append("</").append(tagName).append('>');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HtmlNode htmlNode = (HtmlNode) o;

        return attributes.equals(htmlNode.attributes) && children.equals(htmlNode.children) && tagName.equals(htmlNode.tagName) && textContent.equals(htmlNode.textContent);
    }

    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + textContent.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HtmlNode{" +
                "tagName='" + tagName + '\'' +
                ", attributes=" + attributes +
                ", textContent='" + textContent + '\'' +
                ", children=" + children +
                '}';
    }
}
