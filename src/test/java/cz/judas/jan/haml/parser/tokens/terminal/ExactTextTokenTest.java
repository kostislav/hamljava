package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class ExactTextTokenTest {
    private ExactTextToken token;

    @Before
    public void setUp() throws Exception {
        token = new ExactTextToken("aaa");
    }

    @Test
    public void failsIfStringNotFound() throws Exception {
        assertNotParses(token, "hasc", 1);
    }

    @Test
    public void matchesIfWholeTextIsFound() throws Exception {
        assertParses(token, "haaaaa", 1, "aaa");
    }

    @Test
    public void stopsAtEndOfString() throws Exception {
        assertNotParses(token, "haa", 1);
    }
}
