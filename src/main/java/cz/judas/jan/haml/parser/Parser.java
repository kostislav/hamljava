package cz.judas.jan.haml.parser;

import cz.judas.jan.haml.parser.tokens.Token;

public class Parser<T> {
    private final Token<T> token;

    public Parser(Grammar<T> grammar) {
        token = grammar.buildRules();
    }

    public T parse(String input, T context) {
        token.tryEat(new InputString(input), context);
        return context;
    }
}
