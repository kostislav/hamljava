package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;

public class AnyNumberOfCharToken<T> implements Token<T> {
    private final CharPredicate predicate;

    public AnyNumberOfCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int currentPosition = position;
        while (true) {
            if (currentPosition < line.length() && predicate.test(line.charAt(currentPosition))) {
                currentPosition++;
            } else {
                return currentPosition;
            }
        }
    }
}
