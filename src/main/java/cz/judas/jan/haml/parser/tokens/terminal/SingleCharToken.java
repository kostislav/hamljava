package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class SingleCharToken implements TypedToken<Object, Character> {
    private final CharPredicate predicate;

    public SingleCharToken(char c) {
        predicate = input -> input == c;
    }

    public SingleCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public Optional<Character> tryEat(InputString line) {
        if (line.currentCharIs(predicate)) {
            char matchingChar = line.currentChar();
            line.advance();
            return Optional.of(matchingChar);
        } else {
            return Optional.empty();
        }
    }
}
