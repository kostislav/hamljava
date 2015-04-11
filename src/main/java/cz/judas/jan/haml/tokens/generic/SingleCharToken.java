package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

public class SingleCharToken implements Token<Object> {
    private final CharPredicate predicate;

    public SingleCharToken(char c) {
        predicate = input -> input == c;
    }

    public SingleCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public int tryEat(String line, int position, Object parsingResult) throws ParseException {
        if (line.length() == position || !predicate.test(line.charAt(position))) {
            return -1;
        } else {
            return position + 1;
        }
    }
}
