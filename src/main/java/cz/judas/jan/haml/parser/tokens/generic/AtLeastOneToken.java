package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtLeastOneToken<T> implements Token<T> {
    private final Token<? super T> token;

    public AtLeastOneToken(Token<? super T> token) {
        this.token = token;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int currentPosition = position;
        while(true) {
            int newPosition = token.tryEat(line, currentPosition, parsingResult);
            if(newPosition == -1) {
                if(currentPosition == position) {
                    return -1;
                } else {
                    return currentPosition;
                }
            } else {
                currentPosition = newPosition;
            }
        }
    }
}
