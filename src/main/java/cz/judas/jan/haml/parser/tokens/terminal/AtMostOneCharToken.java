package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtMostOneCharToken<T> implements Token<T> {
    private final CharPredicate predicate;

    public AtMostOneCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        if(line.length() > position && predicate.test(line.charAt(position))) {
            if(line.length() > position + 1 && predicate.test(line.charAt(position + 1))) {
                return -1;
            } else {
                return position + 1;
            }
        } else {
            return position;
        }
    }
}
