package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableSet;
import cz.judas.jan.haml.tokens.Token;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnyOfTokenTest {

    private AnyOfToken<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new AnyOfToken<>(ImmutableSet.<Token<Integer>>of(
                new SingleCharToken<>('a'),
                new SingleCharToken<>('b')
        ));
    }

    @Test
    public void matchesIfFirstTokenMatches() throws Exception {
        assertThat(token.tryEat("podax", 3, 8), is(4));
    }

    @Test
    public void matchesIfSecondTokenMatches() throws Exception {
        assertThat(token.tryEat("pobx", 2, 8), is(3));
    }

    @Test
    public void failsIfNeitherMatches() throws Exception {
        assertThat(token.tryEat("pocem", 2, 8), is(-1));
    }

    @Test
    public void onlyEatsOneTokenWorthOfInput() throws Exception {
        assertThat(token.tryEat("podbaba", 3, 8), is(4));
    }
}
