package cz.judas.jan.haml.tree.mutable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.*;

import java.util.ArrayList;
import java.util.List;

public class MutableHtmlNode implements MutableNode {
    private static final RubySymbol ID_KEY = new RubySymbol("id");
    private static final RubySymbol CLASS_KEY = new RubySymbol("class");

    private String tagName = null;
    private final List<RubyHash> attributes = new ArrayList<>();
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
        attributes.add(RubyHash.singleEntryHash(CLASS_KEY, new RubyString(name)));
    }

    public void setId(RubyExpression id) {
        attributes.add(RubyHash.singleEntryHash(ID_KEY, id));
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
}
