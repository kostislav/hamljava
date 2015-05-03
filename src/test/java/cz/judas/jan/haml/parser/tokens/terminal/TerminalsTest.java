package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.tokens.Token;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class TerminalsTest {
    @Test
    public void whitespaceEatsMuchWhitespace() throws Exception {
        assertParses(Terminals.whitespace(), "hg \t r", 2, " \t ");
    }

    @Test
    public void whitespaceSucceedsEvenIfNoWhitespace() throws Exception {
        assertParses(Terminals.whitespace(), "aaagd", 2, "");
    }

    @Test
    public void strictWhitespaceEatsMuchWhitespace() throws Exception {
        assertParses(Terminals.strictWhitespace(), "hg \t r", 2, " \t ");
    }

    @Test
    public void strictWhitespaceFailsIfNoWhitespace() throws Exception {
        assertNotParses(Terminals.strictWhitespace(), "aaagd", 2);
    }

    @Test
    public void leadingCharSetsValueOnSuccess() throws Exception {
        Token<String> leadingChar = Terminals.leadingChar('.', c -> c == 'a', str -> str);

        assertParses(leadingChar, "bh.aab", 2, "aa");
    }

    @Test
    public void leadingCharDoesNotAllowWhitespace() throws Exception {
        Token<String> leadingChar = Terminals.leadingChar('.', c -> c == 'a', str -> str);

        assertNotParses(leadingChar, "bh. aab", 2);
    }
}
