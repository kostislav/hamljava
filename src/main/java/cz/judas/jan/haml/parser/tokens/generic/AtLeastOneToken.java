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
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(inputString -> token.tryEat(line, parsingResult)) && restMatcher.tryEat(line, parsingResult);
    }
}
