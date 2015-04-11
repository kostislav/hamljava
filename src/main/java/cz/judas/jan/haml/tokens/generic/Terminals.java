package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.anyNumberOf;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.atLeastOne;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.sequence;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate WHITESPACE_PREDICATE = c -> c == ' ' || c == '\t';

    private static final SingleCharToken<Object> SINGLE_WHITESPACE_TOKEN = new SingleCharToken<>(WHITESPACE_PREDICATE);

    private static final Token<Object> RELAXED_WHITESPACE_TOKEN = anyNumberOf(SINGLE_WHITESPACE_TOKEN);

    private static final Token<Object> STRICT_WHITESPACE_TOKEN = atLeastOne(SINGLE_WHITESPACE_TOKEN);

    public static <T> Token<T> whitespace() {
        return (Token<T>) RELAXED_WHITESPACE_TOKEN;
    }

    public static <T> Token<T> strictWhitespace() {
        return (Token<T>) STRICT_WHITESPACE_TOKEN;
    }

    public static <T> Token<T> leadingChar(char leadingChar, CharPredicate validChars, BiConsumer<T, String> onEnd) {
        return sequence(
                new SingleCharToken<T>(leadingChar),
                new OnMatchToken<>(
                        new AtLeastOneToken<>(
                                Terminals.<T>singleChar(validChars)
                        ),
                        onEnd
                )
        );
    }

    public static <T> Token<T> singleChar(char c) {
        return new SingleCharToken<>(c);
    }

    public static <T> Token<T> singleChar(CharPredicate predicate) {
        return new SingleCharToken<>(predicate);
    }
}
