package cz.judas.jan.haml.ruby.reflect;

import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        MethodCall methodCall = new MethodCall(TestObject.class.getMethod("methodWithArgs", int.class));

        assertThat(methodCall.invoke(new TestObject(12, 34), list(2)), is((Object)"abc2"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsException() throws Exception {
        MethodCall methodCall = new MethodCall(TestObject.class.getMethod("brokenNoArgMethod"));

        methodCall.invoke(new TestObject(12, 34), Collections.emptyList());
    }
}
