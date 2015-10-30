package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AdditionalMethods {
    public static final AdditionalMethods EMPTY = new AdditionalMethods(ImmutableMap.of());

    private final Map<Class<?>, Map<String, ? extends AdditionalMethod<?>>> additionalMethods;

    public AdditionalMethods(Map<Class<?>, ? extends Map<String, ? extends AdditionalMethod<?>>> additionalMethods) {
        this.additionalMethods = ImmutableMap.copyOf(additionalMethods);
    }

    @SuppressWarnings("unchecked")
    public <T> AdditionalMethod<T> find(Class<? extends T> targetClass, String name) {
        for (Map.Entry<Class<?>, Map<String, ? extends AdditionalMethod<?>>> entry : additionalMethods.entrySet()) {
            if(entry.getKey().isAssignableFrom(targetClass)) {
                AdditionalMethod<?> method = entry.getValue().get(name);
                if(method != null) {
                    return (AdditionalMethod<T>)method;
                }
            }
        }

        throw new RuntimeException("Method " + name + " not found for class " + targetClass);
    }
}
