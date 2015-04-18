package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AtLeastOneTokenTest {
    private AtLeastOneToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new AtLeastOneToken<>(new SingleCharToken('b'));
    }

    @Test
    public void failsIfNoMatch() throws Exception {
        assertNotParses(token, "lkjh", 2);
    }

    @Test
    public void succeedsOnMatch() throws Exception {
        assertParses(token, "lkjbgf", 3, 4);
    }

    @Test
    public void eatsAllAvailable() throws Exception {
        assertParses(token, "lkjbbbbbgf", 3, 8);
    }
}
