package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AtLeastOneTokenTest {
    private AtLeastOneToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new AtLeastOneToken<>(new SingleCharToken('b'));
    }

    @Test
    public void failsIfNoMatch() throws Exception {
        assertThat(token.tryEat("lkjh", 2, 89), is(-1));
    }

    @Test
    public void succeedsOnMatch() throws Exception {
        assertThat(token.tryEat("lkjbgf", 3, 89), is(4));
    }

    @Test
    public void eatsAllAvailable() throws Exception {
        assertThat(token.tryEat("lkjbbbbbgf", 3, 89), is(8));
    }
}
