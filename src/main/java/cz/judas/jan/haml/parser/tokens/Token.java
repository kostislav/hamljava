package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

public interface Token<T> {
    boolean tryEat(InputString line, T parsingResult);
}
