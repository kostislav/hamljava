package cz.judas.jan.haml.tokens.generic;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhitespaceTokenTest {

    private WhitespaceToken<Integer> token;

    @Before
    public void setUp() throws Exception {
        token = new WhitespaceToken<>();
    }

    @Test
    public void eatsMuchWhitespace() throws Exception {
        assertThat(token.tryEat("hg \t r", 2, 32), is(5));
    }

    @Test
    public void succeedsEvenIfNoWhitespace() throws Exception {
        assertThat(token.tryEat("aaagd", 2, 32), is(2));
    }
}
