package cz.judas.jan.haml;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ParsingResult {
    private String tagName = null;
    private Map<String, String> attributes = new LinkedHashMap<>();
    private Set<String> classes = new LinkedHashSet<>();
    private String content = "";

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public void addClass(String name) {
        classes.add(name);
    }

    private Map<String, String> getAttributes() {
        if (classes.isEmpty()) {
            return attributes;
        } else {
            Map<String, String> copy = new LinkedHashMap<>(attributes);
            copy.put("class", StringUtils.join(classes, ' '));
            return copy;
        }
    }

    public HtmlNode toHtmlNode() {
        return new HtmlNode(
                tagName,
                getAttributes(),
                content
        );
    }
}
