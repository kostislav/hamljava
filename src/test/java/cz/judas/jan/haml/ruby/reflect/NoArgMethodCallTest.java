package cz.judas.jan.haml.ruby.reflect;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NoArgMethodCallTest {
    @Test
    public void callsMethod() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(SomeObject.class.getMethod("getValue1"));

        assertThat(noArgMethodCall.get(new SomeObject(43)), is((Object)43));
    }

    @Test(expected = RuntimeException.class)
    public void rethrowsExceptionIfCallFails() throws Exception {
        NoArgMethodCall noArgMethodCall = new NoArgMethodCall(SomeObject.class.getMethod("brokenMethod"));

        noArgMethodCall.get(new SomeObject(4));
    }

    private static class SomeObject {
        private final int value1;

        private SomeObject(int value1) {
            this.value1 = value1;
        }

        public int getValue1() {
            return value1;
        }

        @SuppressWarnings("UnusedDeclaration")
        public int brokenMethod() {
            throw new IllegalStateException("Is broken");
        }
    }
}
