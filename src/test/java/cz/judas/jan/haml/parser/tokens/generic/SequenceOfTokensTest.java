package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class SequenceOfTokensTest {

    private SequenceOfTokens<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new SequenceOfTokens<>(ImmutableList.of(
                new SingleCharToken('a'),
                new SingleCharToken('b')
        ));
    }

    @Test
    public void matchesIfAllMatch() throws Exception {
        assertParses(token, "svrabec", 3, 5);
    }

    @Test
    public void failsIfNoneMatch() throws Exception {
        assertNotParses(token, "svrfg", 3);
    }

    @Test
    public void failsIfOnlySomeMatch() throws Exception {
        assertNotParses(token, "svratka", 3);
    }
}
