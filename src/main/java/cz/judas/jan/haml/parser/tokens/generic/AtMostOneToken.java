package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class AtMostOneToken<C, T> implements TypedToken<C, Optional<T>> {
    private final Token<? super C> token;

    public AtMostOneToken(Token<? super C> token) {
        this.token = token;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return !(line.tryParse(inputString -> token.tryEat(inputString, parsingResult)) && line.tryParse(inputString -> token.tryEat(inputString, parsingResult)));
    }
}
