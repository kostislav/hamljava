package cz.judas.jan.haml.parser.tokens.terminal;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnyNumberOfCharTokenTest {
    private AnyNumberOfCharToken<Object> token;

    @Before
    public void setUp() throws Exception {
        token = new AnyNumberOfCharToken<>(c -> c == 'p');
    }

    @Test
    public void eatsAllThatIsAvailable() throws Exception {
        assertThat(token.tryEat("apppper", 1, 9), is(5));
    }

    @Test
    public void succeedsEvenIfNoMatch() throws Exception {
        assertThat(token.tryEat("abc", 1, 9), is(1));
    }

    @Test
    public void stopsAtEndOfString() throws Exception {
        assertThat(token.tryEat("appppp", 1, 9), is(6));
    }
}
