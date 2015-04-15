package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AtMostOneCharTokenTest {
    private AtMostOneCharToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new AtMostOneCharToken<>(c -> c == 'o');
    }

    @Test
    public void succeedsIfNoMatch() throws Exception {
        assertThat(token.tryEat("aaaa", 2, 6), is(2));
    }

    @Test
    public void matchesSingleInstance() throws Exception {
        assertThat(token.tryEat("aorta", 1, 6), is(2));
    }

    @Test
    public void failsIfMoreThanOneInstanceEncountered() throws Exception {
        assertThat(token.tryEat("ooo.o", 0, 6), is(-1));
    }

    @Test
    public void worksAlmostAtEndOfString() throws Exception {
        assertThat(token.tryEat("oo", 1, 6), is(2));
    }

    @Test
    public void worksAtEndOfString() throws Exception {
        assertThat(token.tryEat("oo", 2, 6), is(2));
    }
}
