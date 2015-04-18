package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AnyNumberOfCharToken<T> implements Token<T> {
    private final CharPredicate predicate;

    public AnyNumberOfCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.compatibilityMethod(predicate, i -> i >= 0);
    }
}
