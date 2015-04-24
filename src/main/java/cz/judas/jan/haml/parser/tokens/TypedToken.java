package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

public interface TypedToken<C, T> {
    boolean tryEat(InputString line, C parsingResult);
}
