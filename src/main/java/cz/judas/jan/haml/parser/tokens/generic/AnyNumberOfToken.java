package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;

public class AnyNumberOfToken<C, T> implements TypedToken<C, List<T>> {
    private final TypedToken<? super C, ? extends T> inner;

    public AnyNumberOfToken(TypedToken<? super C, ? extends T> inner) {
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
