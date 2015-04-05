package cz.judas.jan.haml;

public interface Node {
    void addChild(Node child);
    void appendTo(StringBuilder stringBuilder);
}
