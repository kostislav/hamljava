package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AtMostOneCharTokenTest {
    private AtMostOneCharToken token;

    @Before
    public void setUp() throws Exception {
        token = new AtMostOneCharToken(c -> c == 'o');
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

    @Test
    public void worksAlmostAtEndOfString() throws Exception {
        assertParses(token, "oo", 1, 2);
    }

    @Test
    public void worksAtEndOfString() throws Exception {
        assertParses(token, "oo", 2, 2);
    }
}
