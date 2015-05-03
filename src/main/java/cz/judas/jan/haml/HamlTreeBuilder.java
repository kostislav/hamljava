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
    private final TypedToken<MutableRootNode, String> doctypeToken;
    private final TypedToken<Object, String> indentToken;
    private final TypedToken<Object, MutableHtmlNode> lineToken;

    public HamlTreeBuilder() {
        HamlGrammar hamlGrammar = new HamlGrammar();
        doctypeToken = hamlGrammar.doctype();
        indentToken = TokenCache.build(hamlGrammar::indent);
        lineToken = TokenCache.build(hamlGrammar::lineContent);
    }

    public RootNode buildTreeFrom(String input) {
        MutableRootNode context = new MutableRootNode();
        InputString line = new InputString(input);
        Optional<String> doctype = doctypeToken.tryEat(line, context);
        doctype.ifPresent(context::setDoctype);

        while (line.hasMoreChars()) {
            context.levelUp(indentToken.tryEat(line, context).get());
            Optional<MutableHtmlNode> result = lineToken.tryEat(line, context);
            if (result.isPresent()) {
                context.addNode(result.get());
            } else {
                break;
            }
        }
        return context.toNode();
    }
}
