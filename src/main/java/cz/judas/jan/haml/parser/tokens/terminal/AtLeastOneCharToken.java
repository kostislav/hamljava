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
    public int tryEat(String line, int position, T parsingResult) {
        return tryEat(new InputString(line, position), parsingResult);
    }

    public int tryEat(InputString line, T parsingResult) {
        return line.compatibilityMethod(predicate, i -> i > 0);
    }
}
