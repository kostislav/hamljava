package cz.judas.jan.haml;

import cz.judas.jan.haml.grammar.HamlGrammar;
import cz.judas.jan.haml.parser.Parser;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import java.util.Optional;

public class HamlTreeBuilder {
    private final Parser<MutableRootNode, Optional<String>> parser = new Parser<>(new HamlGrammar());

    public RootNode buildTreeFrom(String input) {
        MutableRootNode context = new MutableRootNode();
        Optional<String> doctype = parser.parse(input, context);
        doctype.ifPresent(context::setDoctype);
        return context.toNode();
    }
}
