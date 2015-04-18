package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AnyNumberOfCharTokenTest {
    private AnyNumberOfCharToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new AnyNumberOfCharToken<>(c -> c == 'p');
    }

    @Test
    public void eatsAllThatIsAvailable() throws Exception {
        assertParses(token, "apppper", 1, 5);
    }

    @Test
    public void succeedsEvenIfNoMatch() throws Exception {
        assertParses(token, "abc", 1, 1);
    }

    @Test
    public void stopsAtEndOfString() throws Exception {
        assertParses(token, "appppp", 1, 6);
    }
}
