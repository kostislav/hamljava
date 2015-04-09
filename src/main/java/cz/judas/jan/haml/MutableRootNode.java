package cz.judas.jan.haml;

import com.google.common.collect.Iterables;

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
