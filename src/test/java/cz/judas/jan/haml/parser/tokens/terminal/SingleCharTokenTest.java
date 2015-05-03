package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertNotParses;
import static cz.judas.jan.haml.parser.tokens.TokenAssertions.assertParses;

public class SingleCharTokenTest {
    private SingleCharToken token;

    @Before
    public void setUp() throws Exception {
        token = new SingleCharToken('x');
    }

    @Test
    public void succeedsOnSelectedCharacter() throws Exception {
        assertParses(token, "yyxe", 2, 'x');
    }

    @Test
    public void failsOnDifferentCharacter() throws Exception {
        assertNotParses(token, "yyye", 2);
    }

    @Test
    public void failsOnEndOfString() throws Exception {
        assertNotParses(token, "yyye", 4);
    }

    @Test
    public void predicateVersionEatsOneCharFromPredicate() throws Exception {
        token = new SingleCharToken(c -> c == 'a' || c == 'b');

        assertParses(token, "rhbae", 2, 'b');
    }

    @Test
    public void predicateVersionFailsOnAnotherChar() throws Exception {
        token = new SingleCharToken(c -> c == 'a' || c == 'b');

        assertNotParses(token, "rhxae", 2);
    }
}
