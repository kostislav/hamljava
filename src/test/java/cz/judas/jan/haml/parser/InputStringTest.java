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
    public void canPeekAtCurrentChar() throws Exception {
        assertThat(inputString.currentChar(), is('a'));
    }

    @Test
    public void peekDoesNotMoveCursor() throws Exception {
        inputString.currentChar();

        assertThat(inputString.currentChar(), is('a'));
    }

    @Test
    public void nextCharReturnsNextChars() throws Exception {
        assertThat(inputString.nextChar(), is('b'));
        assertThat(inputString.nextChar(), is('c'));
    }

    @Test
    public void peekingAfterNextChar() throws Exception {
        inputString.nextChar();

        assertThat(inputString.currentChar(), is('b'));
    }

    @Test
    public void returnsZeroAtEndOfString() throws Exception {
        inputString.nextChar();
        inputString.nextChar();
        inputString.nextChar();

        assertThat(inputString.nextChar(), is((char)0));
        assertThat(inputString.currentChar(), is((char)0));
    }

    @Test
    public void returnsUnprocessedPart() throws Exception {
        inputString.nextChar();

        assertThat(inputString.unprocessedPart(), is("a"));
    }

    @Test
    public void unprocessedPartMoves() throws Exception {
        inputString.nextChar();
        inputString.unprocessedPart();
        inputString.nextChar();
        inputString.nextChar();

        assertThat(inputString.unprocessedPart(), is("bc"));
    }

    @Test
    public void processingCanBeReverted() throws Exception {
        inputString.takeSnapshot();
        inputString.nextChar();
        inputString.nextChar();

        inputString.revert();

        assertThat(inputString.currentChar(), is('a'));
    }

    @Test
    public void stackedRevertsArePossible() throws Exception {
        inputString.nextChar();

        inputString.takeSnapshot();
        inputString.nextChar();

        inputString.takeSnapshot();
        inputString.nextChar();

        inputString.revert();
        inputString.revert();

        assertThat(inputString.currentChar(), is('b'));
    }

    @Test
    public void substringPositionIsContainedInSnapshot() throws Exception {
        inputString.nextChar();
        inputString.unprocessedPart();

        inputString.takeSnapshot();
        inputString.nextChar();

        inputString.unprocessedPart();

        inputString.revert();
        inputString.nextChar();

        assertThat(inputString.unprocessedPart(), is("b"));
    }

    @Test(expected = IllegalStateException.class)
    public void cannotRevertWithoutTakingSnapshot() throws Exception {
        inputString.revert();
    }
}
