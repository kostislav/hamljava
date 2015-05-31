package cz.judas.jan.haml.ruby.reflect;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyConstants;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
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
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("publicField", TestObject.class);

        assertThat(propertyAccess.get(new TestObject(12, 34), RubyBlock.EMPTY, new HtmlOutput(), MockTemplateContext.EMPTY), is((Object)12));
    }

    @Test
    public void usesGetterForInaccessibleMethods() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("privateField", TestObject.class);

        assertThat(propertyAccess.get(new TestObject(12, 34), RubyBlock.EMPTY, new HtmlOutput(), MockTemplateContext.EMPTY), is((Object)35));
    }

    @Test
    public void callsMethodOtherwise() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("noArgMethod", TestObject.class);

        assertThat(propertyAccess.get(new TestObject(12, 34), RubyBlock.EMPTY, new HtmlOutput(), MockTemplateContext.EMPTY), is((Object) "abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfMethodHasArguments() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("methodWithArgs", TestObject.class);

        assertThat(propertyAccess.get(new TestObject(12, 34), RubyBlock.EMPTY, new HtmlOutput(), MockTemplateContext.EMPTY), is((Object)"abc"));
    }

    @Test
    public void findsAdditionalMethods() throws Exception {
        propertyAccessCreator = new PropertyAccessCreator(ImmutableMultimap.of(
                Iterable.class, new TestAdditionalMethod()
        ));
        HtmlOutput htmlOutput = new HtmlOutput();

        PropertyAccess propertyAccess = propertyAccessCreator.createFor("myMethod", List.class);
        propertyAccess.get(list("a", "b"), RubyBlock.EMPTY, htmlOutput, MockTemplateContext.EMPTY);

        assertThat(htmlOutput.build(), is("added a\nadded b\n"));
    }

    private static class TestAdditionalMethod implements AdditionalMethod<Iterable<?>> {
        @Override
        public Object invoke(Iterable<?> target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            for (Object o : target) {
                htmlOutput.addUnescaped("added " + o, '\n');
            }
            return RubyConstants.NIL;
        }
    }
}
