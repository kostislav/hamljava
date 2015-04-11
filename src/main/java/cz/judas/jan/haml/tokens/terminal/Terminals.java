package cz.judas.jan.haml.tokens.terminal;

import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate WHITESPACE_PREDICATE = c -> c != '\n' && Character.isWhitespace(c);

    private static final SingleCharToken SINGLE_WHITESPACE_TOKEN = new SingleCharToken(WHITESPACE_PREDICATE);

    private static final Token<Object> RELAXED_WHITESPACE_TOKEN = anyNumberOf(SINGLE_WHITESPACE_TOKEN);

    private static final Token<Object> STRICT_WHITESPACE_TOKEN = atLeastOne(SINGLE_WHITESPACE_TOKEN);

    public static Token<Object> whitespace() {
        return RELAXED_WHITESPACE_TOKEN;
    }

    public static Token<Object> strictWhitespace() {
        return STRICT_WHITESPACE_TOKEN;
    }

    public static <T> Token<T> leadingChar(char leadingChar, CharPredicate validChars, BiConsumer<? super T, String> onEnd) {
        return sequence(
                singleChar(leadingChar),
                onMatch(
                        atLeastOne(
                                singleChar(validChars)
                        ),
                        onEnd
                )
        );
    }

    public static Token<Object> singleChar(char c) {
        return new SingleCharToken(c);
    }

    public static Token<Object> singleChar(CharPredicate predicate) {
        return new SingleCharToken(predicate);
    }
}
