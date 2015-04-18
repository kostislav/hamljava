package cz.judas.jan.haml;

import cz.judas.jan.haml.grammar.HamlGrammar;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.tree.RootNode;

public class HamlTreeBuilder {
    private final Token<MutableRootNode> hyperToken = HamlGrammar.hamlDocument();

    public RootNode buildTreeFrom(String input) {
        MutableRootNode mutableRootNode = new MutableRootNode();

        hyperToken.tryEat(new InputString(input), mutableRootNode);

        return mutableRootNode.toNode();
    }
}
