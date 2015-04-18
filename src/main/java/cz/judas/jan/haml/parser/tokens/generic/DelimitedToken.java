package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class DelimitedToken<T> implements Token<T> {
    private final char startDelimiter;
    private final Token<? super T> token;
    private final char endDelimiter;

    public DelimitedToken(char startDelimiter, Token<? super T> token, char endDelimiter) {
        this.startDelimiter = startDelimiter;
        this.token = token;
        this.endDelimiter = endDelimiter;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(inputString -> inputString.advanceIf(startDelimiter) && token.tryEat(inputString, parsingResult) && inputString.advanceIf(endDelimiter));
    }
}
