package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;

public class AtLeastOneToken<C, T> implements TypedToken<C, List<T>> {
    private final Token<? super C> token;
    private final AnyNumberOfToken<? super C, ? extends T> restMatcher;

    public AtLeastOneToken(Token<? super C> token) {
        this.token = token;
        restMatcher = new AnyNumberOfToken<>(token);
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(inputString -> token.tryEat(line, parsingResult)) && restMatcher.tryEat(line, parsingResult);
    }
}
