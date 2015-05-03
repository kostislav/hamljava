package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;

public class ThreeItemSequenceToken<T1, T2, T3, T> implements Token<T> {
    private final Token<? extends T1> firstToken;
    private final Token<? extends T2> secondToken;
    private final Token<? extends T3> thirdToken;
    private final TriFunction<T1, T2, T3, T> transform;

    public ThreeItemSequenceToken(Token<? extends T1> firstToken, Token<? extends T2> secondToken, Token<? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.thirdToken = thirdToken;
        this.transform = transform;
    }

    @Override
    public Optional<T> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            Optional<? extends T1> firstResult = firstToken.tryEat(inputString);
            if(firstResult.isPresent()) {
                Optional<? extends T2> secondResult = secondToken.tryEat(inputString);
                if(secondResult.isPresent()) {
                    Optional<? extends T3> thirdResult = thirdToken.tryEat(inputString);
                    return Optional.of(transform.apply(firstResult.get(), secondResult.get(), thirdResult.get()));
                }
            }
            return Optional.<T>empty();
        });
    }
}
