package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TemplateContextTest {
    @Test
    public void returnsExistingField() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                map("value1", "abcde"),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        assertThat(templateContext.getField("value1"), is((Object)"abcde"));
    }

    @Test
    public void returnsNilForNonExistentField() throws Exception {
        TemplateContext templateContext = new TemplateContext(
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
                UnboundRubyMethod.EMPTY_BLOCK
        );

        assertThat(templateContext.getField("value2"), is((Object) RubyConstants.NIL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsForNonExistentLocalVariable() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                Collections.emptyMap(),
                UnboundRubyMethod.EMPTY_BLOCK
        );

        templateContext.getVariable("abc", AdditionalFunctions.EMPTY, MockHtmlOutput.create());
    }

    @Test
    public void hasExistingVariable() throws Exception {
        TemplateContext parent = new TemplateContext(
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
                UnboundRubyMethod.EMPTY_BLOCK
        );
        TemplateContext child = parent.withLocalVariables(map(
                "abc", 14
        ));

        assertThat(child.hasVariable("abcd"), is(false));
    }
}
