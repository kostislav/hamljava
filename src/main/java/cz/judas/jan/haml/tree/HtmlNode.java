package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.VariableMap;

import java.util.List;
import java.util.Map;

public class HtmlNode implements Node {
    private final String tagName;
    private final Map<RubyExpression, RubyExpression> attributes;
    private final RubyExpression textContent;
    private final List<Node> children;

    public HtmlNode(String tagName, Map<RubyExpression, RubyExpression> attributes, RubyExpression textContent, Iterable<? extends Node> children) {
        this.tagName = tagName;
        this.attributes = ImmutableMap.copyOf(attributes);
        this.textContent = textContent;
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder, VariableMap variableMap) {
        stringBuilder
                .append('<').append(tagName);

        for (Map.Entry<RubyExpression, RubyExpression> entry : attributes.entrySet()) {
            Object attributeName = entry.getKey().evaluate(variableMap);
            Object attributeValue = entry.getValue().evaluate(variableMap);
            stringBuilder.append(' ').append(attributeName).append("=\"").append(attributeValue).append('"');
        }
        stringBuilder.append('>');
        stringBuilder.append(textContent.evaluate(variableMap));
        for (Node child : children) {
            child.appendTo(stringBuilder, variableMap);
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
