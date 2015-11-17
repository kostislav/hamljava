package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CachingMethodFinderTest {

    private TestMethodFinder inner;
    private CachingMethodFinder cachingFinder;

    @Before
    public void setUp() throws Exception {
        inner = new TestMethodFinder();
        cachingFinder = new CachingMethodFinder(inner);
    }

    @Test
    public void returnsMethodIfExists() throws Exception {
        MethodCall method = cachingFinder.findOn(String.class);
        Object result = method.invoke("abc", list(1), UnboundRubyMethod.EMPTY_BLOCK, MockHtmlOutput.create(), MockTemplateContext.EMPTY);

        assertThat(result, is("bc"));
    }

    @Test(expected = RuntimeException.class)
    public void failsIfMethodDoesNotExist() throws Exception {
        cachingFinder.findOn(Integer.class);
    }

    @Test
    public void cachesFoundMethods() throws Exception {
        cachingFinder.findOn(String.class);
        cachingFinder.findOn(String.class);

        inner.assertCalledOnlyOnce();
    }

    @Test
    public void cachesMisses() throws Exception {
        swallowExceptionOf(() -> cachingFinder.findOn(Integer.class));
        swallowExceptionOf(() -> cachingFinder.findOn(Integer.class));

        inner.assertCalledOnlyOnce();
    }

    private static void swallowExceptionOf(Callable<?> block) {
        try {
            block.call();
        } catch (Exception ignored) {
        }
    }

    private static class TestMethodFinder implements MethodFinder {
        private int callCount = 0;
        private final ReflectionMethodCall stringMethod;

        private TestMethodFinder() throws Exception {
            stringMethod = new ReflectionMethodCall(String.class.getMethod("substring", int.class));
        }

        public void assertCalledOnlyOnce() {
            assertThat(callCount, is(1));
        }

        @Override
        public MethodCall findOn(Class<?> targetClass) {
            callCount++;
            if(targetClass.equals(String.class)) {
                return stringMethod;
            } else {
                throw new RuntimeException("Method not found for " + targetClass);
            }
        }
    }
}