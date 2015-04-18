package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtLeastOneCharToken<T> implements Token<T> {
    private final CharPredicate predicate;

    public AtLeastOneCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryGetSubstringIf(predicate, i -> i > 0).isPresent();
    }
}
