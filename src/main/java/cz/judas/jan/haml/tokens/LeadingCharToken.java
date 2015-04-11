package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.generic.AtLeastOneToken;
import cz.judas.jan.haml.tokens.generic.OnMatchToken;

import java.util.function.BiConsumer;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.sequence;

public class LeadingCharToken<T> implements Token<T> {
    private final Token<T> token;

    public LeadingCharToken(char leadingChar, CharPredicate validChars, BiConsumer<T, String> onEnd) {
        token = sequence(
                new SingleCharToken<T>(leadingChar),
                new OnMatchToken<>(
                        new AtLeastOneToken<>(
                                new SingleCharToken<T>(
                                        validChars
                                )
                        ),
                        onEnd
                )
        );
    }

    @Override
    public int tryEat(String line, int position, T mutableHtmlNode) throws ParseException {
        return token.tryEat(line, position, mutableHtmlNode);
    }
}
