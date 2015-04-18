package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;
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
        Optional<String> substring = line.tryParseString(inputString -> token.tryEat(inputString, parsingResult));
        if(substring.isPresent()) {
            onMatch.accept(parsingResult, substring.get());
            return true;
        } else {
            return false;
        }
    }
}
