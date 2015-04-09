package cz.judas.jan.haml.mutabletree;

import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.Node;
import cz.judas.jan.haml.tree.RootNode;

import java.util.*;

public class MutableRootNode implements MutableNode {
    private final List<MutableNode> children = new ArrayList<>();
    private final Deque<MutableNode> stack = new ArrayDeque<>();
    private String doctype = null;

    public MutableRootNode() {
        stack.push(this);
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    @Override
    public void addChild(MutableNode child) {
        children.add(child);
    }

    public void addNode(MutableNode node) {
        stack.peekFirst().addChild(node);
        stack.push(node);
    }

    public void levelUp(int newLevel) {
        while (newLevel < stack.size() - 1) {
            stack.pop();
        }
    }

    @Override
    public Node toNode() {
        return new RootNode(Optional.ofNullable(doctype), Iterables.transform(children, MutableNode::toNode));
    }
}
