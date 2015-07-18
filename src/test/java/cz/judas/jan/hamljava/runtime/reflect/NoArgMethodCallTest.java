package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NoArgMethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(TestObject.class.getMethod("noArgMethod"));

        assertThat(noArgMethodCall.get(new TestObject(43, 54), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)"abc"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsExceptionIfCallFails() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(TestObject.class.getMethod("brokenNoArgMethod"));

        noArgMethodCall.get(new TestObject(4, 5), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY);
    }
}
