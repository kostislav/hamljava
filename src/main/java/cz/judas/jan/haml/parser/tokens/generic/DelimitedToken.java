package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class DelimitedToken<C, T> implements TypedToken<C, T> {
    private final char startDelimiter;
    private final TypedToken<? super C, ? extends T> token;
    private final char endDelimiter;

    public DelimitedToken(char startDelimiter, TypedToken<? super C, ? extends T> token, char endDelimiter) {
        this.startDelimiter = startDelimiter;
        this.token = token;
        this.endDelimiter = endDelimiter;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(inputString -> inputString.advanceIf(startDelimiter) && token.tryEat(inputString, parsingResult) && inputString.advanceIf(endDelimiter));
    }
}
