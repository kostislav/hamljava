package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class RootNode implements Node {
    private final List<Node> children;

    public RootNode(Iterable<? extends Node> children) {
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder) {
        for (Node child : children) {
            child.appendTo(stringBuilder);
        }
    }
}
