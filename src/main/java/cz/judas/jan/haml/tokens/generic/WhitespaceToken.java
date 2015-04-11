package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

public class WhitespaceToken<T> implements Token<T> {
    private final Token<T> token = new AnyNumberOfToken<T>(
            new SingleCharToken<>(c -> c == ' ' || c == '\t')
    );

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        return token.tryEat(line, position, parsingResult);
    }
}
