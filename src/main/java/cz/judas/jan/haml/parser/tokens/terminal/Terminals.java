package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate NOT_NEWLINE_PREDICATE = c -> c != '\n';

    private static final CharPredicate WHITESPACE_PREDICATE = c -> NOT_NEWLINE_PREDICATE.test(c) && Character.isWhitespace(c);

    private static final Token<Object> RELAXED_WHITESPACE_TOKEN = anyNumberOf(WHITESPACE_PREDICATE);

    private static final Token<Object> STRICT_WHITESPACE_TOKEN = atLeastOneChar(WHITESPACE_PREDICATE);

    private static final Token<Object> END_OF_LINE_TOKEN = new EndOfLineToken<>();

    public static Token<Object> whitespace() {
        return RELAXED_WHITESPACE_TOKEN;
    }

    public static Token<Object> strictWhitespace() {
        return STRICT_WHITESPACE_TOKEN;
    }

    public static CharPredicate notNewLine() {
        return NOT_NEWLINE_PREDICATE;
    }

    public static Token<Object> endOfLine() {
        return END_OF_LINE_TOKEN;
    }

    public static <C, T> Token<C> leadingChar(char leadingChar, CharPredicate validChars, BiConsumer<? super C, String> onEnd, Function<String, ? extends T> transform) {
        return GenericTokens.<C, Character, String, T>sequence(
                singleChar(leadingChar),
                onMatch(
                        atLeastOneChar(validChars),
                        onEnd
                ),
                (ignored, value) -> transform.apply(value)
        );
    }

    public static Token<Object> singleChar(char c) {
        return new SingleCharToken(c);
    }

    public static Token<Object> singleChar(CharPredicate predicate) {
        return new SingleCharToken(predicate);
    }

    public static Token<Object> exactText(String value) {
        return new ExactTextToken(value);
    }
}
