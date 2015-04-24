package cz.judas.jan.haml.tree;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.tree.mutable.MutableNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RootNode implements Node {
    private static final Map<String, String> DOCTYPES = ImmutableMap.of(
            "5", "<!DOCTYPE html>"
    );

    private final Optional<String> doctype;
    private final List<Node> children;

    public RootNode(Optional<String> doctype, Iterable<? extends Node> children) {
        this.children = ImmutableList.copyOf(children);
        this.doctype = doctype;
    }

    public RootNode(Optional<String> doctype, Iterable<? extends MutableNode> children, int blbost) {
        this.children = FluentIterable.from(children).transform(mutableNode -> mutableNode.toNode()).toList();
        this.doctype = doctype;
    }

    @Override
    public void appendTo(StringBuilder stringBuilder, VariableMap variableMap) {
        doctype.ifPresent(doctype -> stringBuilder.append(DOCTYPES.get(doctype)).append('\n'));

        for (Node child : children) {
            child.appendTo(stringBuilder, variableMap);
        }
    }

    public String toHtmlString(VariableMap variableMap) {
        StringBuilder stringBuilder = new StringBuilder();
        appendTo(stringBuilder, variableMap);
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RootNode rootNode = (RootNode) o;

        return children.equals(rootNode.children)
                && doctype.equals(rootNode.doctype);
    }

    @Override
    public int hashCode() {
        int result = doctype.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RootNode{" +
                "doctype=" + doctype +
                ", children=" + children +
                '}';
    }
}
