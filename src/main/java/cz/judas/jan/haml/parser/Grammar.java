package cz.judas.jan.haml.parser;

import cz.judas.jan.haml.parser.tokens.TypedToken;

public interface Grammar<C, T> {
    TypedToken<C, T> buildRules();
}
