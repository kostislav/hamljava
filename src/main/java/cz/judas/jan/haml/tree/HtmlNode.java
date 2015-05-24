package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class HtmlNode implements Node {
    private final String tagName;
    private final List<RubyHash> attributes;
    private final RubyExpression textContent;
    private final List<Node> children;

    public HtmlNode(String tagName, List<RubyHash> attributes, RubyExpression textContent, Iterable<? extends Node> children) {
        this.tagName = tagName;
        this.attributes = ImmutableList.copyOf(attributes);
        this.textContent = textContent;
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, VariableMap variableMap) {
        htmlOutput.addUnescaped('<', tagName);

        for (Map.Entry<String, Object> entry : mergeAttributes(htmlOutput, variableMap).entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue;
            if(attributeName.equals("class")) {
                attributeValue = StringUtils.join((Collection<Object>)entry.getValue(), ' ');
            } else {
                attributeValue = entry.getValue();
            }
            htmlOutput.addUnescaped(' ', attributeName, "=\"", attributeValue, '"');
        }

        htmlOutput.addUnescaped('>');
        htmlOutput.addUnescaped(textContent.evaluate(htmlOutput, variableMap));
        for (Node child : children) {
            child.evaluate(htmlOutput, variableMap);
        }
        htmlOutput.addUnescaped("</", tagName, '>');
    }

    private Map<String, Object> mergeAttributes(HtmlOutput htmlOutput, VariableMap variableMap) {
        Map<String, Object> mergedAttributes = new LinkedHashMap<>();
        for (RubyHash hash : attributes) {
            for (Map.Entry<Object, Object> entry : hash.evaluate(htmlOutput, variableMap).entrySet()) {
                String attributeName = entry.getKey().toString();
                Object attributeValue = entry.getValue();
                if(attributeName.equals("class")) {
                    List<Object> classes = (List<Object>)mergedAttributes.get("class");
                    if(classes == null) {
                        classes = new ArrayList<>();
                        mergedAttributes.put("class", classes);
                    }
                    classes.add(attributeValue);
                } else {
                    mergedAttributes.put(attributeName, attributeValue);
                }
            }
        }
        return mergedAttributes;
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
