package cz.judas.jan.haml;

import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.AnyLineToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.Node;

public class HamlParser {
    private final Token<MutableRootNode> hyperToken = new AnyLineToken();

    public String process(String haml) throws ParseException {
        MutableRootNode rootNode = new MutableRootNode();

        for (String line : haml.split("\n")) {
            hyperToken.tryEat(line, 0, rootNode);
        }

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }
}
