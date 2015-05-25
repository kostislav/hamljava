package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.Nil;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHashExpression;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class HtmlNode implements HamlNode {
    private final String tagName;
    private final List<RubyHashExpression> attributes;
    private final RubyExpression textContent;
    private final List<HamlNode> children;

    public HtmlNode(String tagName, List<RubyHashExpression> attributes, RubyExpression textContent, Iterable<? extends HamlNode> children) {
        this.tagName = tagName;
        this.attributes = ImmutableList.copyOf(attributes);
        this.textContent = textContent;
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.addUnescaped('<', tagName);

        for (Map.Entry<String, Object> entry : mergeAttributes(htmlOutput, templateContext).entrySet()) {
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
        htmlOutput.addUnescaped(textContent.evaluate(htmlOutput, templateContext).asString());
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext);
        }
        htmlOutput.addUnescaped("</", tagName, '>');

        return Nil.INSTANCE;
    }

    private Map<String, Object> mergeAttributes(HtmlOutput htmlOutput, TemplateContext templateContext) {
        Map<String, Object> mergedAttributes = new LinkedHashMap<>();
        for (RubyHashExpression hashExpression : attributes) {
            hashExpression.evaluate(htmlOutput, templateContext).each((key, value) -> {
                String attributeName = key.asString();
                if(attributeName.equals("class")) {
                    List<Object> classes = (List<Object>)mergedAttributes.get("class");
                    if(classes == null) {
                        classes = new ArrayList<>();
                        mergedAttributes.put("class", classes);
                    }
                    classes.add(value.asString());
                } else {
                    mergedAttributes.put(attributeName, value.asString());
                }
            });
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
