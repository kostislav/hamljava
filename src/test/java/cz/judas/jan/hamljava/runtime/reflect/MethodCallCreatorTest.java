package cz.judas.jan.hamljava.runtime.reflect;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.methods.AdditionalMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodCallCreatorTest {
    private MethodCallCreator methodCallCreator;

    @Before
    public void setUp() throws Exception {
        methodCallCreator = new MethodCallCreator(ImmutableMultimap.of());
    }

    @Test
    public void findsCorrectMethod() throws Exception {
        MethodCall methodCall = methodCallCreator.createFor(TestObject.class, "methodWithArgs", 2);

        assertThat(methodCall.invoke(new TestObject(1, 2), list(12, "p"), RubyBlock.EMPTY, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)"abc12p"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfMethodDoesNotExist() throws Exception {
        methodCallCreator.createFor(TestObject.class, "madeUpMethod", 1);
    }

    @Test
    public void findsAdditionalMethods() throws Exception {
        methodCallCreator = new MethodCallCreator(ImmutableMultimap.of(
                Iterable.class, new TestAdditionalMethod()
        ));
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        MethodCall propertyAccess = methodCallCreator.createFor(List.class, "myMethod", 1);
        propertyAccess.invoke("kk", list("a"), RubyBlock.EMPTY, htmlOutput, MockTemplateContext.EMPTY);

        assertThat(htmlOutput.build(), is("added kk a"));
    }

    private static class TestAdditionalMethod implements AdditionalMethod<String> {
        @Override
        public Object invoke(String target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            htmlOutput.addUnescaped("added " + target + " " + arguments.get(0));
            return RubyConstants.NIL;
        }
    }
}