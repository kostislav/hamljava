package cz.judas.jan.haml.ruby.reflect;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodCallCreatorTest {
    private MethodCallCreator methodCallCreator;

    @Before
    public void setUp() throws Exception {
        methodCallCreator = new MethodCallCreator();
    }

    @Test
    public void findsCorrectMethod() throws Exception {
        MethodCall methodCall = methodCallCreator.createFor(TestObject.class, "methodWithArgs", 2);

        assertThat(methodCall.invoke(new TestObject(1, 2), list(12, "p")), is((Object)"abc12p"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfMethodDoesNotExist() throws Exception {
        methodCallCreator.createFor(TestObject.class, "madeUpMethod", 1);
    }
}
