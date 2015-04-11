package cz.judas.jan.haml.mutabletree;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.HtmlNode;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.TextNode;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class MutableHtmlNode implements MutableNode {
    private String tagName = null;
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private final Set<String> classes = new LinkedHashSet<>();
    private String id = null;
    private String content = "";

    private final List<MutableNode> children = new ArrayList<>();

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addAttribute(MutableAttribute attribute) {
        attributes.put(attribute.getName(), attribute.getValue());
    }

    public void addClass(String name) {
        classes.add(name);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    @Override
    public Node toNode() {
        if(tagName == null && attributes.isEmpty() && classes.isEmpty() && id == null) {
            return new TextNode(content);
        } else {
            return new HtmlNode(
                    MoreObjects.firstNonNull(tagName, "div"),
                    getAttributes(),
                    content,
                    Iterables.transform(children, MutableNode::toNode)
            );
        }
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
}
