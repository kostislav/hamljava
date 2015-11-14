package cz.judas.jan.hamljava.runtime.methods;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import org.junit.Test;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdditionalFunctionsTest {
    @Test
    public void returnsCorrectFunction() throws Exception {
        UnboundRubyMethod abcFunction = new ConstantFunction("abc");
        AdditionalFunctions functions = new AdditionalFunctions(map(
                "abc", abcFunction,
                "def", new ConstantFunction("def")
        ));

        UnboundRubyMethod function = functions.withName("abc");

        assertThat(function, is(abcFunction));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfFunctionDoesNotExist() throws Exception {
        AdditionalFunctions functions = new AdditionalFunctions(map(
                "def", new ConstantFunction("def")
        ));

        functions.withName("abc");
    }
}