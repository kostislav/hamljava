package cz.judas.jan.haml;

import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.*;
import cz.judas.jan.haml.tree.Node;

public class HamlParser {
    private final Token<MutableRootNode> doctypeToken = new DoctypeToken();
    private final Token<MutableRootNode> lineToken = new LineToken();

    public String process(String haml) throws ParseException {
        MutableRootNode rootNode = new MutableRootNode();

        for (String line : haml.split("\n")) {
            if(doctypeToken.tryEat(line, 0, rootNode) == -1) {
                lineToken.tryEat(line, 0, rootNode);
            }
        }

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }
}
