package cz.judas.jan.haml.ruby.reflect;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyConstants;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.testutil.MockHtmlOutput;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
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
        HtmlOutput htmlOutput = new HtmlOutput();

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
