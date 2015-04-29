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
    public Optional<T> tryEat2(InputString line, C parsingResult) {
        return line.tryParse2(inputString -> {
            if(whitespace.tryEat2(inputString, parsingResult).isPresent()) {
                return (Optional<T>)token.tryEat2(inputString, parsingResult);
            } else {
                return Optional.<T>empty();
            }
        });
    }
}
