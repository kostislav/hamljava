package cz.judas.jan.haml;

import cz.judas.jan.haml.grammar.HamlGrammar;
import cz.judas.jan.haml.parser.Parser;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

public class HamlTreeBuilder {
    private final Parser<MutableRootNode, RootNode> parser = new Parser<>(new HamlGrammar());

    public RootNode buildTreeFrom(String input) {
        return parser.parse(input, new MutableRootNode()).toNode();
    }
}
