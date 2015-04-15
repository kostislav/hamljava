package cz.judas.jan.haml.parser;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InputStringTest {
    private InputString inputString;

    @Before
    public void setUp() throws Exception {
        inputString = new InputString("abcd");
    }

    @Test
    public void returnsTrueIfPredicateMatchesCurrentChar() throws Exception {
        assertThat(inputString.currentCharIs(equalTo('a')), is(true));
    }

    @Test
    public void returnsFalseIfPredicateDoesNotMatchCurrentChar() throws Exception {
        assertThat(inputString.currentCharIs(equalTo('b')), is(false));
    }

    @Test
    public void predicateLooksAtCurrentChar() throws Exception {
        inputString.advance();

        assertThat(inputString.currentCharIs(equalTo('b')), is(true));
    }

    @Test
    public void doesNotMatchAfterEndOfString() throws Exception {
        inputString.advance();
        inputString.advance();
        inputString.advance();
        inputString.advance();

        assertThat(inputString.currentCharIs(equalTo('e')), is(false));
    }

    @Test
    public void tryParseReturnsTrueAndKeepsAdvanceIfMatch() throws Exception {
        inputString.advance();

        boolean result = inputString.tryParse(inputString -> {
            inputString.advance();
            inputString.advance();
            return true;
        });

        assertThat(result, is(true));
        assertCurrentCharIs('d');
    }

    @Test
    public void tryParseReturnsFalseAndRevertsAdvanceIfNoMatch() throws Exception {
        inputString.advance();

        boolean result = inputString.tryParse(inputString -> {
            inputString.advance();
            inputString.advance();
            return false;
        });

        assertThat(result, is(false));
        assertCurrentCharIs('b');
    }

    @Test
    public void tryParsesCanBeNested() throws Exception {
        inputString.advance();

        boolean result = inputString.tryParse(inputString -> {
            inputString.advance();
            inputString.tryParse(inputString2 -> {
                inputString2.advance();
                return false;
            });
            return true;
        });

        assertThat(result, is(true));
        assertCurrentCharIs('c');
    }

    private void assertCurrentCharIs(char c) {
        assertThat(inputString.currentCharIs(equalTo(c)), is(true));
    }

    private static CharPredicate equalTo(char matchingChar) {
        return c -> c == matchingChar;
    }
}
