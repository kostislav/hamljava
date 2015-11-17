package cz.judas.jan.hamljava.runtime.reflect;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.function.Supplier;

public class CachingMethodFinder implements MethodFinder {
    private static final Supplier<MethodCall> NOT_FOUND = () -> { throw new RuntimeException("Method not found"); };
    private final LoadingCache<Class<?>, Supplier<MethodCall>> cache;
    private final MethodFinder inner;

    public CachingMethodFinder(MethodFinder inner) {
        this.inner = inner;
        cache = CacheBuilder.newBuilder().build(CacheLoader.from(this::findForCache));
    }

    @Override
    public MethodCall findOn(Class<?> targetClass) {
        return cache.getUnchecked(targetClass).get();
    }

    private Supplier<MethodCall> findForCache(Class<?> targetClass) {
        try {
            MethodCall methodToCache = inner.findOn(targetClass);
            return () -> methodToCache;
        } catch (Exception e) {
            return NOT_FOUND;
        }
    }
}
