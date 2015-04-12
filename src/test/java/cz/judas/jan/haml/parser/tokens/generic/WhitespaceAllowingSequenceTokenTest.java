package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhitespaceAllowingSequenceTokenTest {
    private WhitespaceAllowingSequenceToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new WhitespaceAllowingSequenceToken<>(ImmutableList.of(
                new SingleCharToken('a'),
                new SingleCharToken('b'),
                new SingleCharToken('c')
        ));
    }

    @Test
    public void allowsWhitespaceBetweenTokens() throws Exception {
        assertThat(token.tryEat("ha   b\tcge", 1, 1000), is(8));
    }

    @Test
    public void whitespaceIsOptional() throws Exception {
        assertThat(token.tryEat("habcge", 1, 1000), is(4));
    }

    @Test
    public void failsIfSequenceDoesNotMatch() throws Exception {
        assertThat(token.tryEat("habe", 1, 1000), is(-1));
    }
}
