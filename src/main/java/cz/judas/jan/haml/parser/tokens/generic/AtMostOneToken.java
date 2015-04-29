package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class AtMostOneToken<C, T> implements TypedToken<C, Optional<T>> {
    private final TypedToken<? super C, ? extends T> token;

    public AtMostOneToken(TypedToken<? super C, ? extends T> token) {
        this.token = token;
    }

    @Override
    public Optional<Optional<T>> tryEat(InputString line, C parsingResult) {
        return line.tryParse2(inputString -> {
            Optional<? extends T> firstResult = token.tryEat(inputString, parsingResult);
            if(firstResult.isPresent()) {
                Optional<? extends T> secondResult = token.tryEat(inputString, parsingResult);
                if(secondResult.isPresent()) {
                    return Optional.empty();
                } else {
                    return Optional.of((Optional<T>)firstResult);
                }
            } else {
                return Optional.of(Optional.<T>empty());
            }
        });
    }
}
