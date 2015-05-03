package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableSet;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.*;

public class AnyOfTokenTest {
    private AnyOfToken<Object, Character> token;

    @Before
    public void setUp() throws Exception {
        token = new AnyOfToken<>(ImmutableSet.of(
                new SingleCharToken('a'),
                new SingleCharToken('b')
        ));
    }

    @Test
    public void matchesIfFirstTokenMatches() throws Exception {
        assertParses(token, "podax", 3, 'a');
    }

    @Test
    public void matchesIfSecondTokenMatches() throws Exception {
        assertParses(token, "pobx", 2, 'b');
    }

    @Test
    public void failsIfNeitherMatches() throws Exception {
        assertNotParses(token, "pocem", 2);
    }

    @Test
    public void onlyEatsOneTokenWorthOfInput() throws Exception {
        assertParses(token, "podbaba", 3, 'b');
    }
}
