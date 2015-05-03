package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;

import java.util.Optional;

public class PrecededWithWhitespaceToken<C, T> implements TypedToken<C, T> {
    private final TypedToken<? super C, String> whitespace;
    private final TypedToken<? super C, ? extends T> token;

    public PrecededWithWhitespaceToken(TypedToken<? super C, ? extends T> token) {
        whitespace = Terminals.whitespace();
        this.token = token;
    }

    @Override
    public Optional<T> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            if (whitespace.tryEat(inputString).isPresent()) {
                return (Optional<T>) token.tryEat(inputString);
            } else {
                return Optional.<T>empty();
            }
        });
    }
}
