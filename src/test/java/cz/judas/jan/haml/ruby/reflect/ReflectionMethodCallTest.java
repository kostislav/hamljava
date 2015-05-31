package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.testutil.MockHtmlOutput;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReflectionMethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        MethodCall reflectionMethodCall = new ReflectionMethodCall(TestObject.class.getMethod("methodWithArgs", int.class));

        assertThat(reflectionMethodCall.invoke(new TestObject(12, 34), list(2), RubyBlock.EMPTY, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)"abc2"));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsException() throws Exception {
        MethodCall reflectionMethodCall = new ReflectionMethodCall(TestObject.class.getMethod("brokenNoArgMethod"));

        reflectionMethodCall.invoke(new TestObject(12, 34), Collections.emptyList(), RubyBlock.EMPTY, MockHtmlOutput.create(), MockTemplateContext.EMPTY);
    }
}
