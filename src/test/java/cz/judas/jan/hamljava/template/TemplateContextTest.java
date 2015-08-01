package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TemplateContextTest {
    @Test
    public void returnsExistingField() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                map("value1", "abcde"),
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        assertThat(templateContext.getField("value1"), is((Object)"abcde"));
    }

    @Test
    public void returnsNilForNonExistentField() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                Collections.emptyMap(),
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        assertThat(templateContext.getField("value1"), is((Object) RubyConstants.NIL));
    }

    @Test
    public void returnsNilForNullField() throws Exception {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("value2", null);
        TemplateContext templateContext = new TemplateContext(
                fields,
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        assertThat(templateContext.getField("value2"), is((Object) RubyConstants.NIL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsForNonExistentLocalVariable() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                Collections.emptyMap(),
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        templateContext.getVariable("abc");
    }

    @Test
    public void hasExistingVariable() throws Exception {
        TemplateContext parent = new TemplateContext(
                Collections.emptyMap(),
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );
        TemplateContext child = parent.withLocalVariables(map(
                "abc", 14
        ));

        assertThat(child.hasVariable("abc"), is(true));
    }

    @Test
    public void doesNotHaveOtherVariables() throws Exception {
        TemplateContext parent = new TemplateContext(
                Collections.emptyMap(),
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );
        TemplateContext child = parent.withLocalVariables(map(
                "abc", 14
        ));

        assertThat(child.hasVariable("abcd"), is(false));
    }

    @Test
    public void returnsCorrectFunction() throws Exception {
        UnboundRubyMethod abcFunction = new ConstantFunction("abc");
        TemplateContext context = new TemplateContext(
                Collections.emptyMap(),
                map(
                        "abc", abcFunction,
                        "def", new ConstantFunction("def")
                ),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        UnboundRubyMethod function = context.getFunction("abc");

        assertThat(function, is(abcFunction));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfFunctionDoesNotExist() throws Exception {
        TemplateContext context = new TemplateContext(
                Collections.emptyMap(),
                map(
                        "def", new ConstantFunction("def")
                ),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        context.getFunction("abc");
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
