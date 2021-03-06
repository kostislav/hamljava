package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReflectionMethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        MethodCall reflectionMethodCall = new ReflectionMethodCall(TestObject.class.getMethod("methodWithArgs", int.class));

        assertThat(reflectionMethodCall.invoke(new TestObject(12, 34), list(2), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)"abc2"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsException() throws Exception {
        MethodCall reflectionMethodCall = new ReflectionMethodCall(TestObject.class.getMethod("brokenNoArgMethod"));

        reflectionMethodCall.invoke(new TestObject(12, 34), Collections.emptyList(), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY);
    }
}
