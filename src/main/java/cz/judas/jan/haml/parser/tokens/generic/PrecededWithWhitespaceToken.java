package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;

import java.util.Optional;

public class PrecededWithWhitespaceToken<T> implements Token<T> {
    private final Token<String> whitespace;
    private final Token<? extends T> token;

    public PrecededWithWhitespaceToken(Token<? extends T> token) {
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
