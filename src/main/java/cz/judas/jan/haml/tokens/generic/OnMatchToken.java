package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;

public class OnMatchToken<T> implements Token<T> {
    private final Token<? super T> token;
    private final BiConsumer<? super T, String> onMatch;

    public OnMatchToken(Token<? super T> token, BiConsumer<? super T, String> onMatch) {
        this.token = token;
        this.onMatch = onMatch;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int newPosition = token.tryEat(line, position, parsingResult);
        if(newPosition != -1) {
            onMatch.accept(parsingResult, line.substring(position, newPosition));
        }
        return newPosition;
    }
}
