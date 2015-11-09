package cz.judas.jan.hamljava.runtime.methods;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.TemplateContext;
import org.junit.Test;

import java.util.List;

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

    private static class ConstantFunction implements UnboundRubyMethod {
        private final String value;

        private ConstantFunction(String value) {
            this.value = value;
        }

        @Override
        public Object invoke(List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            return value;
        }
    }
}