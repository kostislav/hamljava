package cz.judas.jan.haml;

import java.util.ArrayList;
import java.util.List;

public class RootNode implements Node {
    private final List<Node> children = new ArrayList<>();

    @Override
    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder) {
        for (Node child : children) {
            child.appendTo(stringBuilder);
        }
    }
}
