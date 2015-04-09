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
}
