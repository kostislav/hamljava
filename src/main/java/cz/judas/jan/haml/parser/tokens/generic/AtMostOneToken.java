package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtMostOneToken<T> implements Token<T> {
    private final Token<? super T> token;

    public AtMostOneToken(Token<? super T> token) {
        this.token = token;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return !line.tryParse(inputString -> token.tryEat(inputString, parsingResult)) || !line.tryParse(inputString -> token.tryEat(inputString, parsingResult));
    }
}
