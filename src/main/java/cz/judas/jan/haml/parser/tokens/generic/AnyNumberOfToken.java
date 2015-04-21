package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;

public class AnyNumberOfToken<C, T> implements TypedToken<C, List<T>> {
    private final Token<? super C> inner;

    public AnyNumberOfToken(Token<? super C> inner) {
        this.inner = inner;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        while(line.hasMoreChars()) {
            if(!line.tryParse(inputString -> inner.tryEat(inputString, parsingResult))) {
                break;
            }
        }
        return true;
    }
}
