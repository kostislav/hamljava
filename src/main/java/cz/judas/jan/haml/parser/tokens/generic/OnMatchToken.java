package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;
import java.util.function.BiConsumer;

public class OnMatchToken<C, T> implements TypedToken<C, T> {
    private final TypedToken<? super C, ? extends T> token;
    private final BiConsumer<? super C, String> onMatch;

    public OnMatchToken(TypedToken<? super C, ? extends T> token, BiConsumer<? super C, String> onMatch) {
        this.token = token;
        this.onMatch = onMatch;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        Optional<String> substring = line.tryParseString(inputString -> token.tryEat(inputString, parsingResult));
        if(substring.isPresent()) {
            onMatch.accept(parsingResult, substring.get());
            return true;
        } else {
            return false;
        }
    }
}
