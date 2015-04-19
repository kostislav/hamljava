package cz.judas.jan.haml.tree.mutable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MutableHtmlNode implements MutableNode {
    private static final RubySymbol ID_KEY = new RubySymbol("id");

    private String tagName = null;
    private final List<RubyHash> attributes = new ArrayList<>();
    private final Set<String> classes = new LinkedHashSet<>();
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
        attributes.add(singleValueHash(ID_KEY, id));
    }

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    @Override
    public Node toNode() {
        if(tagName == null && attributes.isEmpty() && classes.isEmpty()) {
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

    private List<RubyHash> getAttributes() {
        ImmutableList.Builder<RubyHash> builder = ImmutableList.builder();
        builder.addAll(attributes);
        if(!classes.isEmpty()) {
            builder.add(singleValueHash(new RubySymbol("class"), new RubyString(StringUtils.join(classes, ' ')))); // TODO array
        }
        return builder.build();
    }

    private RubyHash singleValueHash(RubyExpression key, RubyExpression value) {
        return new RubyHash(ImmutableList.of(new HashEntry(key, value)));
    }
}
