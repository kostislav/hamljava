package cz.judas.jan.haml;

public interface MutableNode {
    void addChild(MutableNode child);

    Node toNode();
}
