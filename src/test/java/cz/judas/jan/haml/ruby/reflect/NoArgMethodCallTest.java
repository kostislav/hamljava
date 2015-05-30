package cz.judas.jan.haml.ruby.reflect;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NoArgMethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(TestObject.class.getMethod("noArgMethod"));

        assertThat(noArgMethodCall.get(new TestObject(43, 54)), is((Object)"abc"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsExceptionIfCallFails() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(TestObject.class.getMethod("brokenNoArgMethod"));

        noArgMethodCall.get(new TestObject(4, 5));
    }
}
