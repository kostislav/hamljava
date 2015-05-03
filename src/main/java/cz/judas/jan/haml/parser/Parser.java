package cz.judas.jan.haml.parser;

import cz.judas.jan.haml.parser.tokens.TokenCache;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class Parser<C, T> {
    private final TypedToken<C, T> token;

    public Parser(Grammar<C, T> grammar) {
        token = TokenCache.build(grammar);
    }

    public T parse(String input, C context) {
        return token.tryEat(new InputString(input), context).get();
    }
}
