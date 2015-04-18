package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

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
        assertParses(token, "ha   b\tcge", 1, 8);
    }

    @Test
    public void whitespaceIsOptional() throws Exception {
        assertParses(token, "habcge", 1, 4);
    }

    @Test
    public void failsIfSequenceDoesNotMatch() throws Exception {
        assertNotParses(token, "habe", 1);
    }
}
