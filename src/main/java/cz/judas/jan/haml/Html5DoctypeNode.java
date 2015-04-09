package cz.judas.jan.haml;

public class Html5DoctypeNode implements Node, MutableNode {
    @Override
    public void appendTo(StringBuilder stringBuilder) {
        stringBuilder.append("<!DOCTYPE html>\n");
    }

    @Override
    public void addChild(MutableNode child) {
        throw new UnsupportedOperationException("A doctype node cannot have nested content");
    }

    @Override
    public Html5DoctypeNode toNode() {
        return this;
    }
}
