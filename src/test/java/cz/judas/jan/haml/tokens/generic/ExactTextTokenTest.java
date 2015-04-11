package cz.judas.jan.haml.tokens.generic;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExactTextTokenTest {
    private ExactTextToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new ExactTextToken<>("aaa");
    }

    @Test
    public void failsIfStringNotFound() throws Exception {
        assertThat(token.tryEat("hasc", 1, 98), is(-1));
    }

    @Test
    public void matchesIfWholeTextIsFound() throws Exception {
        assertThat(token.tryEat("haaaaa", 1, 98), is(4));
    }

    @Test
    public void stopsAtEndOfString() throws Exception {
        assertThat(token.tryEat("haa", 1, 98), is(-1));
    }
}