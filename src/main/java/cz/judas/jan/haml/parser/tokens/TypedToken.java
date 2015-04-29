package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

public interface TypedToken<C, T> {
    Optional<T> tryEat(InputString line, C parsingResult);
}
