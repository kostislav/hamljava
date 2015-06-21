package cz.judas.jan.haml.template.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.template.tree.ruby.RubyHashExpression;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class HtmlNode implements HamlNode {
    private final String tagName;
    private final List<RubyHashExpression> attributes;
    private final HamlNode content;
    private final List<HamlNode> children;

    public HtmlNode(String tagName, List<RubyHashExpression> attributes, HamlNode content, Iterable<? extends HamlNode> children) {
        this.tagName = tagName;
        this.attributes = ImmutableList.copyOf(attributes);
        this.content = content;
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.addChar('<').addUnescaped(tagName);

        Attributes attributes = mergeAttributes(htmlOutput, templateContext);
        if(attributes.hasId()) {
            htmlOutput.addUnescaped(" id=\"").add(attributes.joinedIds()).addChar('"');
        }
        if (attributes.hasClasses()) {
            htmlOutput.addUnescaped(" class=\"").add(attributes.joinedClasses()).addChar('"');
        }
        for (Map.Entry<String, Object> entry : attributes.otherAttributes().entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();
            htmlOutput.addChar(' ').add(attributeName).addUnescaped("=\"").add(attributeValue).addChar('"');
        }

        htmlOutput.addChar('>');
        content.evaluate(htmlOutput, templateContext);
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext);
        }
        htmlOutput.addUnescaped("</").addUnescaped(tagName).addChar('>');
    }

    private Attributes mergeAttributes(HtmlOutput htmlOutput, TemplateContext templateContext) {
        Map<String, Object> mergedAttributes = new LinkedHashMap<>();
        List<String> classes = new ArrayList<>(); // TODO object
        List<String> ids = new ArrayList<>();
        for (RubyHashExpression hashExpression : attributes) {
            Map<?, ?> attributes = hashExpression.evaluate(htmlOutput, templateContext);
            attributes.forEach((key, value) -> {
                String attributeName = key.toString();
                switch (attributeName) {
                    case "class":
                        classes.add(value.toString());
                        break;
                    case "id":
                        ids.add(value.toString());
                        break;
                    default:
                        mergedAttributes.put(attributeName, value.toString());
                        break;
                }
            });
        }
        return new Attributes(classes, ids, mergedAttributes);
    }

    private static class Attributes {
        private final List<String> classes;
        private final List<String> ids;
        private final Map<String, Object> attributes;

        private Attributes(List<String> classes, List<String> ids, Map<String, ?> attributes) {
            this.classes = ImmutableList.copyOf(classes);
            this.ids = ImmutableList.copyOf(ids);
            this.attributes = ImmutableMap.copyOf(attributes);
        }

        public boolean hasClasses() {
            return !classes.isEmpty();
        }

        public String joinedClasses() {
            return String.join(" ", classes);
        }

        public boolean hasId() {
            return !ids.isEmpty();
        }

        public String joinedIds() {
            return String.join("_", ids);
        }

        public Map<String, Object> otherAttributes() {
            return ImmutableMap.copyOf(Maps.filterKeys(attributes, key -> !key.equals("id")));
        }
    }
}
