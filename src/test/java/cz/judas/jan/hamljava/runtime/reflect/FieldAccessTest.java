package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FieldAccessTest {
    @Test
    public void callsMethod() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(TestObject.class.getField("publicField"));

        assertThat(fieldAccess.get(new TestObject(43, 54), RubyBlock.EMPTY, MockHtmlOutput.create(), MockTemplateContext.EMPTY), is((Object)43));
    }

    @Test(expected = RuntimeException.class)
    public void failsIfInaccessible() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(TestObject.class.getDeclaredField("privateField"));

        fieldAccess.get(new TestObject(4, 5), RubyBlock.EMPTY, MockHtmlOutput.create(), MockTemplateContext.EMPTY);
    }
}
