package cz.judas.jan.haml.parsing;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AlternativesTest {
    @Test
    public void usesFirstIfNotNull() throws Exception {
        String result = Alternatives
                .either(6, value -> "x" + value)
                .or(7, value -> "y" + value)
                .orException();

        assertThat(result, is("x6"));
    }

    @Test
    public void usesSecondIfFirstIsNull() throws Exception {
        String result = Alternatives
                .either((Integer)null, value -> "x" + value)
                .or(7, value -> "y" + value)
                .orException();

        assertThat(result, is("y7"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfAllAreNull() throws Exception {
        Alternatives
                .either((Integer)null, value -> "x" + value)
                .or((Integer)null, value -> "y" + value)
                .orException();
    }

    @Test
    public void usesDefaultIfAllAreNull() throws Exception {
        String result = Alternatives
                .either((Integer)null, value -> "x" + value)
                .or((Integer)null, value -> "y" + value)
                .orDefault("0");

        assertThat(result, is("0"));
    }
}
