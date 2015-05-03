package cz.judas.jan.haml;

import cz.judas.jan.haml.grammar.HamlGrammar;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TokenCache;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import java.util.Optional;

public class HamlTreeBuilder {
    private final TypedToken<MutableRootNode, Optional<String>> token;

    public HamlTreeBuilder() {
        token = TokenCache.build(new HamlGrammar());
    }

    public RootNode buildTreeFrom(String input) {
        MutableRootNode context = new MutableRootNode();
        Optional<String> doctype = token.tryEat(new InputString(input), context).get();
        doctype.ifPresent(context::setDoctype);
        return context.toNode();
    }
}
