package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequenceOfTokensTest {

    private SequenceOfTokens<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new SequenceOfTokens<>(ImmutableList.of(
                new SingleCharToken('a'),
                new SingleCharToken('b')
        ));
    }

    @Test
    public void matchesIfAllMatch() throws Exception {
        assertThat(token.tryEat("svrabec", 3, 99), is(5));
    }

    @Test
    public void failsIfNoneMatch() throws Exception {
        assertThat(token.tryEat("svrfg", 3, 99), is(-1));
    }

    @Test
    public void failsIfOnlySomeMatch() throws Exception {
        assertThat(token.tryEat("svratka", 3, 99), is(-1));
    }
}
