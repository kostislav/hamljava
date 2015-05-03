package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

public interface Token<T> {
    Optional<? extends T> tryEat(InputString line);
}
