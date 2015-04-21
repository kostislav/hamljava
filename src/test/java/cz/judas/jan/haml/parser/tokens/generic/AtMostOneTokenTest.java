package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AtMostOneTokenTest {
    private AtMostOneToken<Object, Character> token;

    @Before
    public void setUp() throws Exception {
        token = new AtMostOneToken<>(new SingleCharToken('o'));
    }

    @Test
    public void succeedsIfNoMatch() throws Exception {
        assertParses(token, "aaaa", 2, 2);
    }

    @Test
    public void matchesSingleInstance() throws Exception {
        assertParses(token, "aorta", 1, 2);
    }

    @Test
    public void failsIfMoreThanOneInstanceEncountered() throws Exception {
        assertNotParses(token, "ooo.o", 0);
    }
}
