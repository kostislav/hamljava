package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AtMostOneTokenTest {

    private AtMostOneToken<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new AtMostOneToken<>(new SingleCharToken<Integer>('o'));
    }

    @Test
    public void succeedsIfNoMatch() throws Exception {
        assertThat(token.tryEat("aaaa", 2, 6), is(2));
    }

    @Test
    public void matchesSingleInstance() throws Exception {
        assertThat(token.tryEat("aorta", 1, 6), is(2));
    }

    @Test(expected = ParseException.class)
    public void failsIfMoreThanOneInstanceEncountered() throws Exception {
        token.tryEat("ooo.o", 0, 6); // TODO should just fail?
    }
}