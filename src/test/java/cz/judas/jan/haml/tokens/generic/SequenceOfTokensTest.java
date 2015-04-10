package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.SingleCharToken;
import cz.judas.jan.haml.tokens.Token;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequenceOfTokensTest {

    private SequenceOfTokens<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new SequenceOfTokens<>(ImmutableList.<Token<Integer>>of(
                new SingleCharToken<>('a'),
                new SingleCharToken<>('b')
        ));
    }

    @Test
    public void matchesIfAllMatch() throws Exception {
        assertThat(token.tryEat("svrabec", 3, 99), is(5));
    }

    @Test(expected = ParseException.class)
    public void failsIfNoneMatch() throws Exception {
        token.tryEat("svrfg", 3, 99); // TODO should just fail?
    }

    @Test(expected = ParseException.class)
    public void failsIfOnlySomeMatch() throws Exception {
        token.tryEat("svratka", 3, 99); // TODO should just fail?
    }
}
