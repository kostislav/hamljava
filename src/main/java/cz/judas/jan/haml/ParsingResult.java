package cz.judas.jan.haml;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ParsingResult {
    private String tagName = "div";
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private final Set<String> classes = new LinkedHashSet<>();
    private String id = null;
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

    public void setId(String id) {
        this.id = id;
    }

    private Map<String, String> getAttributes() {
        if (classes.isEmpty() && id == null) {
            return attributes;
        } else {
            Map<String, String> copy = new LinkedHashMap<>(attributes);
            if(id != null) {
                copy.put("id", id);
            }
            if(!classes.isEmpty()) {
                copy.put("class", StringUtils.join(classes, ' '));
            }
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
