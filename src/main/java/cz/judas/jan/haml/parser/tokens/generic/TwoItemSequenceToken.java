package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;
import java.util.function.BiFunction;

public class TwoItemSequenceToken<T1, T2, T> implements Token<T> {
    private final Token<? extends T1> firstToken;
    private final Token<? extends T2> secondToken;
    private final BiFunction<T1, T2, T> transform;

    public TwoItemSequenceToken(Token<? extends T1> firstToken, Token<? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.transform = transform;
    }

    @Override
    public Optional<T> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            Optional<? extends T1> firstResult = firstToken.tryEat(inputString);
            if(firstResult.isPresent()) {
                Optional<? extends T2> secondResult = secondToken.tryEat(inputString);
                if(secondResult.isPresent()) {
                    return Optional.of(transform.apply(firstResult.get(), secondResult.get()));
                }
            }
            return Optional.<T>empty();
        });
    }
}
