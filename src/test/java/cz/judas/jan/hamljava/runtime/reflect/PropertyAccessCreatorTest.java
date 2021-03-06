package cz.judas.jan.hamljava.runtime.reflect;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.methods.AdditionalMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.List;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertyAccessCreatorTest {
    private PropertyAccessCreator propertyAccessCreator;

    @Before
    public void setUp() throws Exception {
        propertyAccessCreator = new PropertyAccessCreator(ImmutableMultimap.of());
    }

    @Test
    public void directlyAccessesPublicFields() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor(TestObject.class, "publicField");

        assertThat(propertyAccess.get(new TestObject(12, 34), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)12));
    }

    @Test
    public void usesGetterForInaccessibleMethods() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor(TestObject.class, "privateField");

        assertThat(propertyAccess.get(new TestObject(12, 34), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)35));
    }

    @Test
    public void callsMethodOtherwise() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor(TestObject.class, "noArgMethod");

        assertThat(propertyAccess.get(new TestObject(12, 34), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object) "abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfMethodHasArguments() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor(TestObject.class, "methodWithArgs");

        assertThat(propertyAccess.get(new TestObject(12, 34), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)"abc"));
    }

    @Test
    public void findsAdditionalMethods() throws Exception {
        propertyAccessCreator = new PropertyAccessCreator(ImmutableMultimap.of(
                Iterable.class, new TestAdditionalMethod()
        ));
        StringWriter writer = new StringWriter();
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(writer, false);

        PropertyAccess propertyAccess = propertyAccessCreator.createFor(List.class, "myMethod");
        propertyAccess.get(list("a", "b"), UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, MockTemplateContext.EMPTY);

        assertThat(writer.toString(), is("added a\nadded b\n"));
    }

    private static class TestAdditionalMethod implements AdditionalMethod<Iterable<?>> {
        @Override
        public Object invoke(Iterable<?> target, List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            for (Object o : target) {
                htmlOutput.addUnescaped("added " + o + '\n');
            }
            return RubyConstants.NIL;
        }
    }
}
