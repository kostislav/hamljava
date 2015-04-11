package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.Node;

public interface MutableNode {
    void addChild(MutableNode child);

    Node toNode();
}
