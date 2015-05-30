package cz.judas.jan.haml.ruby.reflect;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FieldAccessTest {
    @Test
    public void callsMethod() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(TestObject.class.getField("publicField"));

        assertThat(fieldAccess.get(new TestObject(43, 54)), is((Object)43));
    }

    @Test(expected = RuntimeException.class)
    public void failsIfInaccessible() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(TestObject.class.getDeclaredField("privateField"));

        fieldAccess.get(new TestObject(4, 5));
    }
}
