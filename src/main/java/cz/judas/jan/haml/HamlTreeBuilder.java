package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tokens.DocumentToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.RootNode;

public class HamlTreeBuilder {
    private final Token<MutableRootNode> hyperToken = new DocumentToken();

    public RootNode buildTreeFrom(String input) throws ParseException {
        MutableRootNode mutableRootNode = new MutableRootNode();

        hyperToken.tryEat(input, 0, mutableRootNode);

        return mutableRootNode.toNode();
    }
}
