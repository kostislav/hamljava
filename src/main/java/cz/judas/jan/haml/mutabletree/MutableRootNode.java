package cz.judas.jan.haml.mutabletree;

import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;

import java.util.ArrayList;
import java.util.List;

public class MutableRootNode implements MutableNode {
    private final List<MutableNode> children = new ArrayList<>();

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    @Override
    public Node toNode() {
        return new RootNode(Iterables.transform(children, MutableNode::toNode));
    }
}
