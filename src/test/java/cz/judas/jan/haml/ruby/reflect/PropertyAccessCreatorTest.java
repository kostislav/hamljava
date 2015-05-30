package cz.judas.jan.haml.ruby.reflect;

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
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("publicField", SomeObject.class);

        assertThat(propertyAccess.get(new SomeObject(12, 34)), is((Object)12));
    }

    @Test
    public void usesGetterForInaccessibleMethods() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("privateField", SomeObject.class);

        assertThat(propertyAccess.get(new SomeObject(12, 34)), is((Object)35));
    }

    @Test
    public void callsMethodOtherwise() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("noArgMethod", SomeObject.class);

        assertThat(propertyAccess.get(new SomeObject(12, 34)), is((Object) "abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfMethodHasArguments() throws Exception {
        PropertyAccess propertyAccess = propertyAccessCreator.createFor("methodWithArgs", SomeObject.class);

        assertThat(propertyAccess.get(new SomeObject(12, 34)), is((Object)"abc"));
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class SomeObject {
        public final int publicField;
        private final int privateField;

        private SomeObject(int publicField, int privateField) {
            this.publicField = publicField;
            this.privateField = privateField;
        }

        public int getPrivateField() {
            return privateField + 1;
        }

        public String noArgMethod() {
            return "abc";
        }

        public String methodWithArgs(int value) {
            return "abc" + value;
        }
    }
}
