package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class AtMostOneToken<T> implements Token<Optional<T>> {
    private final TypedToken<?, ? extends T> token;

    public AtMostOneToken(TypedToken<?, ? extends T> token) {
        this.token = token;
    }

    @Override
    public Optional<Optional<T>> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            Optional<? extends T> firstResult = token.tryEat(inputString);
            if(firstResult.isPresent()) {
                Optional<? extends T> secondResult = token.tryEat(inputString);
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
