package cz.judas.jan.haml;

import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.DocumentToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.RootNode;

public class HamlParser {
    private final Token<MutableRootNode> hyperToken = new DocumentToken();

    public String process(String haml) throws ParseException {
        MutableRootNode mutableRootNode = new MutableRootNode();

        hyperToken.tryEat(haml + "\n", 0, mutableRootNode); //TODO remove string creation

        RootNode rootNode = mutableRootNode.toNode();
        return rootNode.toHtmlString();
    }
}
