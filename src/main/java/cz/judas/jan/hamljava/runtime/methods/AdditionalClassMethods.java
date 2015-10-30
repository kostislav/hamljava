package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public class AdditionalClassMethods<T> {
    private final Class<?> clazz;
    private final Map<String, AdditionalMethod<T>> additionalMethods;

    public AdditionalClassMethods(Class<?> clazz, Map<String, ? extends AdditionalMethod<T>> additionalMethods) {
        this.clazz = clazz;
        this.additionalMethods = ImmutableMap.copyOf(additionalMethods);
    }

    public Class<?> getEnhancedClass() {
        return clazz;
    }

    public Optional<AdditionalMethod<T>> withName(String name) {
        return Optional.of(additionalMethods.get(name));
    }
}
