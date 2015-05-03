package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("UtilityClass")
public class TokenAssertions {
    public static <T> void assertParses(Token<? extends T> token, String input, int position, T expectedResult) {
        InputString inputString = new InputString(input, position);

        Optional<? extends T> result = token.tryEat(inputString);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(equalTo(expectedResult)));
    }

    public static void assertNotParses(Token<?> token, String input, int position) {
        InputString inputString = new InputString(input, position);

        Optional<?> result = token.tryEat(inputString);

        assertThat(result.isPresent(), is(false));
    }
}
