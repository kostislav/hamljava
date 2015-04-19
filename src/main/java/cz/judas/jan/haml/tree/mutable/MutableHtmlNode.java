package cz.judas.jan.haml.tree.mutable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.*;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class MutableHtmlNode implements MutableNode {
    private String tagName = null;
    private final List<RubyHash> attributes = new ArrayList<>();
    private final Set<String> classes = new LinkedHashSet<>();
    private RubyExpression id = null;
    private RubyExpression content = RubyString.EMPTY;

    private final List<MutableNode> children = new ArrayList<>();

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setContent(RubyExpression content) {
        this.content = content;
    }

    public void addAttributes(RubyHash attributes) {
        this.attributes.add(attributes);
    }

    public void addClass(String name) {
        classes.add(name);
    }

    public void setId(RubyExpression id) {
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

    private Map<RubyExpression, RubyExpression> getAttributes() {
        ImmutableMap.Builder<RubyExpression, RubyExpression> builder = ImmutableMap.builder();
        if (classes.isEmpty() && id == null) {
            for (RubyHash hash : attributes) {
                hash.forEach(builder::put);
            }
        } else {
            if(id != null) {
                builder.put(new RubySymbol("id"), id);
            }
            if(!classes.isEmpty()) {
                builder.put(new RubySymbol("class"), new RubyString(StringUtils.join(classes, ' '))); // TODO array
            }
            for (RubyHash hash : attributes) {
                hash.forEach(builder::put);
            }
        }
        return builder.build();
    }
}
