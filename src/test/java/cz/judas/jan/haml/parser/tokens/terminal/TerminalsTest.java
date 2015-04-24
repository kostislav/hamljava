package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.tokens.TypedToken;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TerminalsTest {
    @Test
    public void whitespaceEatsMuchWhitespace() throws Exception {
        assertParses(Terminals.whitespace(), "hg \t r", 2, 5);
    }

    @Test
    public void whitespaceSucceedsEvenIfNoWhitespace() throws Exception {
        assertParses(Terminals.whitespace(), "aaagd", 2, 2);
    }

    @Test
    public void strictWhitespaceEatsMuchWhitespace() throws Exception {
        assertParses(Terminals.strictWhitespace(), "hg \t r", 2, 5);
    }

    @Test
    public void strictWhitespaceFailsIfNoWhitespace() throws Exception {
        assertNotParses(Terminals.strictWhitespace(), "aaagd", 2);
    }

    @Test
    public void leadingCharSetsValueOnSuccess() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        TypedToken<StringBuilder, ?> leadingChar = Terminals.leadingChar('.', c -> c == 'a', StringBuilder::append, str -> str);

        assertParses(leadingChar, "bh.aab", 2, stringBuilder, 5);
        assertThat(stringBuilder.toString(), is("aa"));
    }

    @Test
    public void leadingCharDoesNotAllowWhitespace() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        TypedToken<StringBuilder, ?> leadingChar = Terminals.leadingChar('.', c -> c == 'a', StringBuilder::append, str -> str);

        assertNotParses(leadingChar, "bh. aab", 2, stringBuilder);
        assertThat(stringBuilder.length(), is(0));
    }
}
