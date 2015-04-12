package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.tokens.Token;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TerminalsTest {
    @Test
    public void whitespaceEatsMuchWhitespace() throws Exception {
        assertThat(Terminals.whitespace().tryEat("hg \t r", 2, 32), is(5));
    }

    @Test
    public void whitespaceSucceedsEvenIfNoWhitespace() throws Exception {
        assertThat(Terminals.whitespace().tryEat("aaagd", 2, 32), is(2));
    }

    @Test
    public void strictWhitespaceEatsMuchWhitespace() throws Exception {
        assertThat(Terminals.strictWhitespace().tryEat("hg \t r", 2, 32), is(5));
    }

    @Test
    public void strictWhitespaceFailsIfNoWhitespace() throws Exception {
        assertThat(Terminals.strictWhitespace().tryEat("aaagd", 2, 32), is(-1));
    }

    @Test
    public void leadingCharSetsValueOnSuccess() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Token<StringBuilder> leadingChar = Terminals.leadingChar('.', c -> c == 'a', StringBuilder::append);

        assertThat(leadingChar.tryEat("bh.aab", 2, stringBuilder), is(5));
        assertThat(stringBuilder.toString(), is("aa"));
    }

    @Test
    public void leadingCharDoesNotAllowWhitespace() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Token<StringBuilder> leadingChar = Terminals.leadingChar('.', c -> c == 'a', StringBuilder::append);

        assertThat(leadingChar.tryEat("bh. aab", 2, stringBuilder), is(-1));
        assertThat(stringBuilder.length(), is(0));
    }
}
