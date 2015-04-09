package cz.judas.jan.haml.mutabletree;

import cz.judas.jan.haml.tree.Node;

public interface MutableNode {
    void addChild(MutableNode child);

    Node toNode();
}
