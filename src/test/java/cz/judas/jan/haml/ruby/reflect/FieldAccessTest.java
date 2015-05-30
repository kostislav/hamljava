package cz.judas.jan.haml.ruby.reflect;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FieldAccessTest {
    @Test
    public void callsMethod() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(SomeObject.class.getField("bleh"));

        assertThat(fieldAccess.get(new SomeObject(43)), is((Object)43));
    }

    @Test(expected = RuntimeException.class)
    public void failsIfInaccessible() throws Exception {
        FieldAccess fieldAccess = new FieldAccess(SomeObject.class.getDeclaredField("blah"));

        fieldAccess.get(new SomeObject(4));
    }

    private static class SomeObject {
        public final int bleh;
        @SuppressWarnings("UnusedDeclaration")
        private final int blah = 89;

        private SomeObject(int bleh) {
            this.bleh = bleh;
        }
    }
}
