package cz.judas.jan.haml.mutabletree;

import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MutableRootNode implements MutableNode {
    private final List<MutableNode> children = new ArrayList<>();
    private final Deque<MutableNode> stack = new ArrayDeque<>();

    public MutableRootNode() {
        stack.push(this);
    }

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    public void addNode(MutableNode node) {
        stack.peekFirst().addChild(node);
        stack.push(node);
    }

    public int nestingLevel() {
        return stack.size() - 1;
    }

    public void levelUp() {
        stack.pop();
    }

    @Override
    public Node toNode() {
        return new RootNode(Iterables.transform(children, MutableNode::toNode));
    }
}
