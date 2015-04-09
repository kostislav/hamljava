package cz.judas.jan.haml;

import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.DocumentToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.Node;

public class HamlParser {
    private final Token<MutableRootNode> hyperToken = new DocumentToken();

    public String process(String haml) throws ParseException {
        MutableRootNode rootNode = new MutableRootNode();

        hyperToken.tryEat(haml + "\n", 0, rootNode); //TODO remove string creation

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }
}
