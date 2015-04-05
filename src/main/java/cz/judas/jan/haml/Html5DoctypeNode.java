package cz.judas.jan.haml;

public class Html5DoctypeNode implements Node {
    @Override
    public void toString(StringBuilder stringBuilder) {
        stringBuilder.append("<!DOCTYPE html>\n");
    }

    @Override
    public void addChild(Node child) {
        throw new UnsupportedOperationException("A doctype node cannot have nested content");
    }
}
