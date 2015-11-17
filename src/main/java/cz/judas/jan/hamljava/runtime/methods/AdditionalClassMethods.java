package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public class AdditionalClassMethods<T> {
    private final Class<? extends T> clazz;
    private final Map<String, AdditionalMethod<T>> additionalMethods;

    private AdditionalClassMethods(Class<? extends T> clazz, Map<String, ? extends AdditionalMethod<T>> additionalMethods) {
        this.clazz = clazz;
        this.additionalMethods = ImmutableMap.copyOf(additionalMethods);
    }

    public Class<? extends T> getEnhancedClass() {
        return clazz;
    }

    public Optional<AdditionalMethod<T>> withName(String name) {
        return Optional.ofNullable(additionalMethods.get(name));
    }

    public static <T> AdditionalClassMethods<T> forClass(Class<? extends T> clazz, Map<String, ? extends AdditionalMethod<T>> additionalMethods) {
        return new AdditionalClassMethods<>(clazz, additionalMethods);
    }

    @SuppressWarnings("unchecked") // Class<? extends T> is not a supertype of Class<? extends T<?>>
    public static <T> AdditionalClassMethods<T> forGenericClass(Class<?> clazz, Map<String, ? extends AdditionalMethod<T>> additionalMethods) {
        return new AdditionalClassMethods<>((Class<? extends T>)clazz, additionalMethods);
    }
}
