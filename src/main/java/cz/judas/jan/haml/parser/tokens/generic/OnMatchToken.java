package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;
import java.util.function.BiConsumer;

public class OnMatchToken<C> implements TypedToken<C, String> {
    private final TypedToken<? super C, String> token;
    private final BiConsumer<? super C, String> onMatch;

    public OnMatchToken(TypedToken<? super C, String> token, BiConsumer<? super C, String> onMatch) {
        this.token = token;
        this.onMatch = onMatch;
    }

    @Override
    public Optional<String> tryEat2(InputString line, C parsingResult) {
        Optional<String> substring = line.tryParseString(inputString -> token.tryEat(inputString, parsingResult));
        if(substring.isPresent()) {
            onMatch.accept(parsingResult, substring.get());
            return Optional.of(substring.get());
        } else {
            return Optional.empty();
        }
    }
}
