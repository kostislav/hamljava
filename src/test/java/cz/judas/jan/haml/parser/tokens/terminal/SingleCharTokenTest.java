package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SingleCharTokenTest {

    private SingleCharToken token;

    @Before
    public void setUp() throws Exception {
        token = new SingleCharToken('x');
    }

    @Test
    public void succeedsOnSelectedCharacter() throws Exception {
        assertThat(token.tryEat("yyxe", 2, 0), is(3));
    }

    @Test
    public void failsOnDifferentCharacter() throws Exception {
        assertThat(token.tryEat("yyye", 2, 0), is(-1));
    }

    @Test
    public void failsOnEndOfString() throws Exception {
        assertThat(token.tryEat("yyye", 4, 0), is(-1));
    }

    @Test
    public void predicateVersionEatsOneCharFromPredicate() throws Exception {
        token = new SingleCharToken(c -> c == 'a' || c == 'b');

        assertThat(token.tryEat("rhbae", 2, 0), is(3));
    }

    @Test
    public void predicateVersionFailsOnAnotherChar() throws Exception {
        token = new SingleCharToken(c -> c == 'a' || c == 'b');

        assertThat(token.tryEat("rhxae", 2, 0), is(-1));
    }
}
