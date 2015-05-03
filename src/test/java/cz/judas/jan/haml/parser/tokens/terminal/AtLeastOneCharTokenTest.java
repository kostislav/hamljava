package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AtLeastOneCharTokenTest {
    private AtLeastOneCharToken token;

    @Before
    public void setUp() throws Exception {
        token = new AtLeastOneCharToken(c -> c == 'b');
    }

    @Test
    public void failsIfNoMatch() throws Exception {
        assertNotParses(token, "lkjh", 2);
    }

    @Test
    public void succeedsOnMatch() throws Exception {
        assertParses(token, "lkjbgf", 3, "b");
    }

    @Test
    public void eatsAllAvailable() throws Exception {
        assertParses(token, "lkjbbbbbgf", 3, "bbbbb");
    }

    @Test
    public void stopsAtEnd() throws Exception {
        assertParses(token, "lkjbbbbb", 3, "bbbbb");
    }
}
