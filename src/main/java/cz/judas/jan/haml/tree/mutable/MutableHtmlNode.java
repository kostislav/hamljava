package cz.judas.jan.haml.tree.mutable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutableHtmlNode implements MutableNode {
    private String tagName = null;
    private final List<RubyHash> attributes;
    private RubyExpression content = RubyString.EMPTY;

    private final List<MutableNode> children = new ArrayList<>();

    public MutableHtmlNode(String tagName, List<RubyHash> attributes, RubyExpression content) {
        this.tagName = tagName;
        this.attributes = ImmutableList.copyOf(attributes);
        this.content = content;
    }

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    @Override
    public Node toNode() {
        if(tagName == null && attributes.isEmpty()) {
            return new TextNode(content);
        } else {
            return new HtmlNode(
                    MoreObjects.firstNonNull(tagName, "div"),
                    attributes,
                    content,
                    Iterables.transform(children, MutableNode::toNode)
            );
        }
    }

    public static MutableHtmlNode textNode(RubyExpression content) {
        return new MutableHtmlNode(null, Collections.emptyList(), content);
    }
}
