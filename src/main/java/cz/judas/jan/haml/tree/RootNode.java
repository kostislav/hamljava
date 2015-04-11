package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public class RootNode implements Node {
    private final Optional<String> doctype;
    private final List<Node> children;

    public RootNode(Optional<String> doctype, Iterable<? extends Node> children) {
        this.children = ImmutableList.copyOf(children);
        this.doctype = doctype;
    }

    @Override
    public void appendTo(StringBuilder stringBuilder) {
        doctype.ifPresent(doctype -> stringBuilder.append(doctype).append('\n'));

        for (Node child : children) {
            child.appendTo(stringBuilder);
        }
    }

    public String toHtmlString() {
        StringBuilder stringBuilder = new StringBuilder();
        appendTo(stringBuilder);
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
