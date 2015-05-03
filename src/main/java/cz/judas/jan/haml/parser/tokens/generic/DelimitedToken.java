package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class DelimitedToken<C, T> implements TypedToken<C, T> {
    private final char startDelimiter;
    private final TypedToken<?, ? extends T> token;
    private final char endDelimiter;

    public DelimitedToken(char startDelimiter, TypedToken<?, ? extends T> token, char endDelimiter) {
        this.startDelimiter = startDelimiter;
        this.token = token;
        this.endDelimiter = endDelimiter;
    }

    @Override
    public Optional<T> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            if(inputString.advanceIf(startDelimiter)) {
                Optional<? extends T> result = token.tryEat(inputString);
                if(result.isPresent() && inputString.advanceIf(endDelimiter)) {
                    return (Optional<T>)result;
                }
            }
            return Optional.empty();
        });
    }
}
