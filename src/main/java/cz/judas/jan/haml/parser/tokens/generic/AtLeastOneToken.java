package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class AtLeastOneToken<T> implements Token<T> {
    private final Token<? super T> token;
    private final AnyNumberOfToken<? super T> restMatcher;

    public AtLeastOneToken(Token<? super T> token) {
        this.token = token;
        restMatcher = new AnyNumberOfToken<>(token);
    }

    @Override
    public int tryEat(InputString line, T parsingResult) {
        if(!line.tryParse(inputString -> token.tryEat(line, parsingResult) != -1)) {
            return -1;
        }
        return restMatcher.tryEat(line, parsingResult);
    }
}
