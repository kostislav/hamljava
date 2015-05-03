package cz.judas.jan.haml.tree.mutable;

import com.google.common.collect.Iterables;
import cz.judas.jan.haml.tree.RootNode;

import java.util.*;

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

    public void addNode(MutableHtmlNode node) {
        stack.peekFirst().addChild(node);
        stack.push(node);
    }

    public void levelUp(String newPrefix) {
        while (newPrefix.length() < stack.size() - 1) {
            stack.pop();
        }
    }

    @Override
    public RootNode toNode() {
        throw new UnsupportedOperationException("Use the other toNode method");
    }

    public RootNode toNode(Optional<String> doctype) {
        return new RootNode(doctype, Iterables.transform(children, MutableNode::toNode));
    }
}
