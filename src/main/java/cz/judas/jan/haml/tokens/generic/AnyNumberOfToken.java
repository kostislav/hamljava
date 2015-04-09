package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

public class AnyNumberOfToken<T> implements Token<T> {
    private final Token<T> inner;

    public AnyNumberOfToken(Token<T> inner) {
        this.inner = inner;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int currentPosition = position;
        while(currentPosition < line.length()) {
            int newPosition = inner.tryEat(line, currentPosition, parsingResult);
            if(newPosition == -1) {
                return currentPosition;
            } else {
                currentPosition = newPosition;
            }
        }
        return currentPosition;
    }
}
