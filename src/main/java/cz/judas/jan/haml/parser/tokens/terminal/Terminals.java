package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;
import java.util.function.Function;

import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;

@SuppressWarnings({"UtilityClass", "unchecked"})
public class Terminals {
    private static final CharPredicate NOT_NEWLINE_PREDICATE = c -> c != '\n';

    private static final CharPredicate WHITESPACE_PREDICATE = c -> NOT_NEWLINE_PREDICATE.test(c) && Character.isWhitespace(c);

    private static final TypedToken<Object, String> RELAXED_WHITESPACE_TOKEN = anyNumberOfChars(WHITESPACE_PREDICATE);

    private static final TypedToken<Object, String> STRICT_WHITESPACE_TOKEN = atLeastOneChar(WHITESPACE_PREDICATE);

    private static final TypedToken<Object, Optional<String>> END_OF_LINE_TOKEN = new EndOfLineToken();

    public static TypedToken<Object, String> whitespace() {
        return RELAXED_WHITESPACE_TOKEN;
    }

    public static TypedToken<Object, String> strictWhitespace() {
        return STRICT_WHITESPACE_TOKEN;
    }

    public static CharPredicate notNewLine() {
        return NOT_NEWLINE_PREDICATE;
    }

    public static TypedToken<Object, Optional<String>> endOfLine() {
        return END_OF_LINE_TOKEN;
    }

    public static <C, T> TypedToken<C, T> leadingChar(char leadingChar, CharPredicate validChars, Function<String, ? extends T> transform) {
        return sequence(
                singleChar(leadingChar),
                atLeastOneChar(validChars),
                (ignored, value) -> transform.apply(value)
        );
    }

    public static TypedToken<Object, Character> singleChar(char c) {
        return new SingleCharToken(c);
    }

    public static TypedToken<Object, Character> singleChar(CharPredicate predicate) {
        return new SingleCharToken(predicate);
    }

    public static TypedToken<Object, String> exactText(String value) {
        return new ExactTextToken(value);
    }
}
