package cz.judas.jan.haml.parser;

import cz.judas.jan.haml.parser.tokens.Token;

public interface Grammar<T> {
    Token<T> buildRules();
}
