package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

public interface TypedToken<C, T> {
    default Optional<T> tryEat2(InputString line, C parsingResult) {
        throw new UnsupportedOperationException("must be implemented");
    }

    default boolean tryEat(InputString line, C parsingResult) {
        return tryEat2(line, parsingResult).isPresent();
    }
}
