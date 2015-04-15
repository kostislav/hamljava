package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtMostOneToken<T> implements Token<T> {
    private final Token<? super T> token;

    public AtMostOneToken(Token<? super T> token) {
        this.token = token;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int newPosition = token.tryEat(line, position, parsingResult);
        if(newPosition != -1 && token.tryEat(line, newPosition, parsingResult) != -1) {
            return -1;
        }
        if(newPosition == -1) {
            return position;
        } else {
            return newPosition;
        }
    }
}