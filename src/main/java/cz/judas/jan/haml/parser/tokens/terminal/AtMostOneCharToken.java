package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtMostOneCharToken<T> implements Token<T> {
    private final CharPredicate predicate;

    public AtMostOneCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryGetSubstringIf(predicate, i -> i <= 1).isPresent();
    }
}
