package cz.judas.jan.haml.runtime;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NilTest {
    @Test
    public void toStringIsEmpty() throws Exception {
        Nil nil = new Nil();

        assertThat(nil.toString(), is(""));
    }
}
