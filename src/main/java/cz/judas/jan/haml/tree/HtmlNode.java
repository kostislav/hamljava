package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import cz.judas.jan.haml.ruby.RubyConstants;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHashExpression;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
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
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.add('<').addUnescaped(tagName);

        Attributes attributes = mergeAttributes(htmlOutput, templateContext);
        if(attributes.hasId()) {
            htmlOutput.addUnescaped(" id=\"").add(attributes.id()).add('"');
        }
        if (attributes.hasClasses()) {
            htmlOutput.addUnescaped(" class=\"").add(StringUtils.join(attributes.classes(), ' ')).add('"');
        }
        for (Map.Entry<String, Object> entry : attributes.otherAttributes().entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();
            htmlOutput.add(' ').add(attributeName).addUnescaped("=\"").add(attributeValue).add('"');
        }

        htmlOutput.addUnescaped('>');
        htmlOutput.addUnescaped(textContent.evaluate(htmlOutput, templateContext));
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext);
        }
        htmlOutput.addUnescaped("</").addUnescaped(tagName).add('>');

        return RubyConstants.NIL;
    }

    private Attributes mergeAttributes(HtmlOutput htmlOutput, TemplateContext templateContext) {
        Map<String, Object> mergedAttributes = new LinkedHashMap<>();
        List<String> classes = new ArrayList<>();
        for (RubyHashExpression hashExpression : attributes) {
            Map<?, ?> attributes = hashExpression.evaluate(htmlOutput, templateContext);
            attributes.forEach((key, value) -> {
                String attributeName = key.toString();
                if (attributeName.equals("class")) {
                    classes.add(value.toString());
                } else {
                    mergedAttributes.put(attributeName, value.toString());
                }
            });
        }
        return new Attributes(classes, mergedAttributes);
    }

    private static class Attributes {
        private final List<String> classes;
        private final Map<String, Object> attributes;

        private Attributes(List<String> classes, Map<String, ?> attributes) {
            this.classes = ImmutableList.copyOf(classes);
            this.attributes = ImmutableMap.copyOf(attributes);
        }

        public boolean hasClasses() {
            return !classes.isEmpty();
        }

        public List<String> classes() {
            return ImmutableList.copyOf(classes);
        }

        public boolean hasId() {
            return attributes.containsKey("id");
        }

        public String id() {
            return attributes.get("id").toString();
        }

        public Map<String, Object> otherAttributes() {
            return ImmutableMap.copyOf(Maps.filterKeys(attributes, key -> !key.equals("id")));
        }
    }
}
