package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.function.BiConsumer;

public class OnMatchToken<T> implements Token<T> {
    private final Token<? super T> token;
    private final BiConsumer<? super T, String> onMatch;

    public OnMatchToken(Token<? super T> token, BiConsumer<? super T, String> onMatch) {
        this.token = token;
        this.onMatch = onMatch;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        String substring = line.tryGetSubstring(inputString -> token.tryEat(inputString, parsingResult));
        if(substring != null) {
            onMatch.accept(parsingResult, substring);
            return true;
        } else {
            return false;
        }
    }
}
