package cz.judas.jan.haml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlNode implements Node {
    private String tagName;
    private Map<String, String> attributes;
    private String textContent;
    private List<Node> children = new ArrayList<>();

    public HtmlNode(String tagName, Map<String, String> attributes, String textContent) {
        this.tagName = tagName;
        this.attributes = attributes;
        this.textContent = textContent;
    }

    @Override
    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder) {
        stringBuilder
                .append('<').append(tagName);

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            stringBuilder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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
}
