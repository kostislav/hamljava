package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.tokens.Token;

public class AnyNumberOfToken<T> implements Token<T> {
    private final Token<? super T> inner;

    public AnyNumberOfToken(Token<? super T> inner) {
        this.inner = inner;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) {
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
