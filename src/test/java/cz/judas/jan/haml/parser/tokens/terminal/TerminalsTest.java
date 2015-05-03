package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.tokens.TypedToken;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.*;

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
        TypedToken<Object, String> leadingChar = Terminals.leadingChar('.', c -> c == 'a', str -> str);

        assertParses2(leadingChar, "bh.aab", 2, "aa");
    }

    @Test
    public void leadingCharDoesNotAllowWhitespace() throws Exception {
        TypedToken<Object, ?> leadingChar = Terminals.leadingChar('.', c -> c == 'a', str -> str);

        assertNotParses(leadingChar, "bh. aab", 2);
    }
}
