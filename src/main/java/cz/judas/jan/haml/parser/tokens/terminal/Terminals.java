package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;
import java.util.function.Function;

import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate NOT_NEWLINE_PREDICATE = c -> c != '\n';

    private static final CharPredicate WHITESPACE_PREDICATE = c -> NOT_NEWLINE_PREDICATE.test(c) && Character.isWhitespace(c);

    private static final Token<String> RELAXED_WHITESPACE_TOKEN = anyNumberOfChars(WHITESPACE_PREDICATE);

    private static final Token<String> STRICT_WHITESPACE_TOKEN = atLeastOneChar(WHITESPACE_PREDICATE);

    private static final Token<Optional<String>> END_OF_LINE_TOKEN = new EndOfLineToken();

    public static Token<String> whitespace() {
        return RELAXED_WHITESPACE_TOKEN;
    }

    public static Token<String> strictWhitespace() {
        return STRICT_WHITESPACE_TOKEN;
    }

    public static CharPredicate notNewLine() {
        return NOT_NEWLINE_PREDICATE;
    }

    public static Token<Optional<String>> endOfLine() {
        return END_OF_LINE_TOKEN;
    }

    public static <T> Token<T> leadingChar(char leadingChar, CharPredicate validChars, Function<String, ? extends T> transform) {
        return sequence(
                singleChar(leadingChar),
                atLeastOneChar(validChars),
                (ignored, value) -> transform.apply(value)
        );
    }

    public static Token<Character> singleChar(char c) {
        return new SingleCharToken(c);
    }

    public static Token<Character> singleChar(CharPredicate predicate) {
        return new SingleCharToken(predicate);
    }

    public static Token<String> exactText(String value) {
        return new ExactTextToken(value);
    }
}
