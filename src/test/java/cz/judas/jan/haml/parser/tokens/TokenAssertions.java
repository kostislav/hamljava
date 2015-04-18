package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("UtilityClass")
public class TokenAssertions {
    public static void assertParses(Token<Object> token, String input, int position, int expectedEndPosition) {
        assertParses(token, input, position, 7, expectedEndPosition);
    }

    public static <T> void assertParses(Token<? super T> token, String input, int position, T context, int expectedEndPosition) {
        InputString inputString = new InputString(input, position);

        boolean result = token.tryEat(inputString, context);

        assertThat(result, is(true));
        assertThat(inputString.currentPosition(), is(expectedEndPosition));
    }

    public static void assertNotParses(Token<Object> token, String input, int position) {
        assertNotParses(token, input, position, 7);
    }

    public static <T> void assertNotParses(Token<? super T> token, String input, int position, T context) {
        InputString inputString = new InputString(input, position);

        boolean result = token.tryEat(inputString, context);

        assertThat(result, is(false));
    }
}
