package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AnyNumberOfToken<T> implements Token<T> {
    private final Token<? super T> inner;

    public AnyNumberOfToken(Token<? super T> inner) {
        this.inner = inner;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        while(line.hasMoreChars()) {
            if(!line.tryParse(inputString -> inner.tryEat(inputString, parsingResult))) {
                break;
            }
        }
        return true;
    }
}
