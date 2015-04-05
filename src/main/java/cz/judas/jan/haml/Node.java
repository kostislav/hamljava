package cz.judas.jan.haml;

public interface Node {
    void addChild(Node child);
    void toString(StringBuilder stringBuilder);
}
