package cz.judas.jan.haml;

import cz.judas.jan.haml.grammar.HamlGrammar;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TokenCache;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.tree.RootNode;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import java.util.Optional;

public class HamlTreeBuilder {
    private final TypedToken<?, String> doctypeToken;
    private final TypedToken<?, String> indentToken;
    private final TypedToken<?, MutableHtmlNode> lineToken;

    public HamlTreeBuilder() {
        HamlGrammar hamlGrammar = new HamlGrammar();
        doctypeToken = hamlGrammar.doctype();
        indentToken = hamlGrammar.indent();
        lineToken = TokenCache.build(hamlGrammar::lineContent);
    }

    public RootNode buildTreeFrom(String input) {
        MutableRootNode context = new MutableRootNode();
        InputString line = new InputString(input);
        Optional<String> doctype = doctypeToken.tryEat(line);
        doctype.ifPresent(context::setDoctype);

        while (line.hasMoreChars()) {
            context.levelUp(indentToken.tryEat(line).get());
            Optional<MutableHtmlNode> result = lineToken.tryEat(line);
            if (result.isPresent()) {
                context.addNode(result.get());
            } else {
                break;
            }
        }
        return context.toNode();
    }
}
