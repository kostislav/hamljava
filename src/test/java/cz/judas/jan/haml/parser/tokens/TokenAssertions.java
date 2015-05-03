package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("UtilityClass")
public class TokenAssertions {
    public static void assertParses(TypedToken<Object, ?> token, String input, int position, int expectedEndPosition) {
        InputString inputString = new InputString(input, position);

        Optional<?> result = token.tryEat(inputString);

        assertThat(result.isPresent(), is(true));
        assertThat(inputString.currentPosition(), is(expectedEndPosition));
    }

    public static <T> void assertParses2(TypedToken<Object, ? extends T> token, String input, int position, T expectedResult) { // TODO
        InputString inputString = new InputString(input, position);

        Optional<? extends T> result = token.tryEat(inputString);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(equalTo(expectedResult)));
    }

    public static void assertNotParses(TypedToken<Object, ?> token, String input, int position) {
        InputString inputString = new InputString(input, position);

        Optional<?> result = token.tryEat(inputString);

        assertThat(result.isPresent(), is(false));
    }
}
