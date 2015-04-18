package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.function.BiConsumer;

import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate NOT_NEWLINE_PREDICATE = c -> c != '\n';

    private static final CharPredicate WHITESPACE_PREDICATE = c -> NOT_NEWLINE_PREDICATE.test(c) && Character.isWhitespace(c);

    private static final Token<Object> RELAXED_WHITESPACE_TOKEN = anyNumberOf(WHITESPACE_PREDICATE);

    private static final Token<Object> STRICT_WHITESPACE_TOKEN = atLeastOneChar(WHITESPACE_PREDICATE);

    public static Token<Object> whitespace() {
        return RELAXED_WHITESPACE_TOKEN;
    }

    public static Token<Object> strictWhitespace() {
        return STRICT_WHITESPACE_TOKEN;
    }

    public static CharPredicate notNewLine() {
        return NOT_NEWLINE_PREDICATE;
    }

    public static <T> Token<T> leadingChar(char leadingChar, CharPredicate validChars, BiConsumer<? super T, String> onEnd) {
        return sequence(
                singleChar(leadingChar),
                onMatch(
                        atLeastOneChar(validChars),
                        onEnd
                )
        );
    }

    public static Token<Object> singleChar(char c) {
        return new SingleCharToken(c);
    }

    public static Token<Object> exactText(String value) {
        return new ExactTextToken(value);
    }
}
