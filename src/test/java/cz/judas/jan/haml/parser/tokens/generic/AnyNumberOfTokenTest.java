package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class AnyNumberOfTokenTest {
    private AnyNumberOfToken<Character> token;

    @Before
    public void setUp() throws Exception {
        token = new AnyNumberOfToken<>(new SingleCharToken('p'));
    }

    @Test
    public void eatsAllThatIsAvailable() throws Exception {
        assertParses(token, "apppper", 1, ImmutableList.of('p', 'p', 'p', 'p'));
    }

    @Test
    public void succeedsEvenIfNoMatch() throws Exception {
        assertParses(token, "abc", 1, Collections.emptyList());
    }

    @Test
    public void stopsAtEndOfString() throws Exception {
        assertParses(token, "appppp", 1, ImmutableList.of('p', 'p', 'p', 'p', 'p'));
    }
}
