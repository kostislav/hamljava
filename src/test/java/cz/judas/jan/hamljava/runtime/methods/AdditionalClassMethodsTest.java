package cz.judas.jan.hamljava.runtime.methods;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdditionalClassMethodsTest {
    @Test
    public void returnsMethodIfFound() throws Exception {
        AdditionalClassMethods<String> methods = AdditionalClassMethods.forClass(String.class, map(
                "addx", (target, arguments, block, htmlOutput, templateContext) -> target + "x"
        ));

        Object result = methods.withName("addx").get().invoke("a", Collections.emptyList(), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY);

        assertThat(result, is("ax"));
    }

    @Test
    public void returnsEmptyIfNotFound() throws Exception {
        AdditionalClassMethods<String> methods = AdditionalClassMethods.forClass(String.class, map(
                "addx", (target, arguments, block, htmlOutput, templateContext) -> target + "x"
        ));

        assertThat(methods.withName("addy"), is(Optional.empty()));
    }
}