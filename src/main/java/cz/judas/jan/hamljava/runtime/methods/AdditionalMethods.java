package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class AdditionalMethods {
    public static final AdditionalMethods EMPTY = new AdditionalMethods(Collections.emptySet());

    private final Map<Class<?>, AdditionalClassMethods<?>> additionalMethods;

    public AdditionalMethods(Collection<? extends AdditionalClassMethods<?>> additionalMethods) {
        ImmutableMap.Builder<Class<?>, AdditionalClassMethods<?>> builder = ImmutableMap.<Class<?>, AdditionalClassMethods<?>>builder();
        for (AdditionalClassMethods<?> additionalClassMethods : additionalMethods) {
            builder.put(additionalClassMethods.getEnhancedClass(), additionalClassMethods);
        }
        this.additionalMethods = builder.build();
    }

    @SuppressWarnings("unchecked")
    public <T> AdditionalMethod<T> find(Class<? extends T> targetClass, String name) {
        for (Map.Entry<Class<?>, AdditionalClassMethods<?>> entry : additionalMethods.entrySet()) {
            if(entry.getKey().isAssignableFrom(targetClass)) {
                Optional<? extends AdditionalMethod<?>> method = entry.getValue().withName(name);
                if(method.isPresent()) {
                    return (AdditionalMethod<T>)method.get();
                }
            }
        }

        throw new RuntimeException("Method " + name + " not found for " + targetClass);
    }
}
