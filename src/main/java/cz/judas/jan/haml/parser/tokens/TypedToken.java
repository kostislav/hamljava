package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

public interface TypedToken<C, T> {
    default Optional<T> tryEat2(InputString line, C parsingResult) {
        if(tryEat(line, parsingResult)) {
            return Optional.of((T)new Object());
        } else {
            return Optional.empty();
        }
    }

    default boolean tryEat(InputString line, C parsingResult) {
        return tryEat2(line, parsingResult).isPresent();
    }
}
