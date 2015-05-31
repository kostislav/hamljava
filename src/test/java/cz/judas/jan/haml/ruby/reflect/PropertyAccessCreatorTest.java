package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertyAccessCreatorTest {

    private PropertyAccessCreator propertyAccessCreator;

    @Before
    public void setUp() throws Exception {
        propertyAccessCreator = new PropertyAccessCreator();
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
}
