package cz.judas.jan.haml.tokens;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SingleCharTokenTest {

    private SingleCharToken<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new SingleCharToken<>('x');
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
}
