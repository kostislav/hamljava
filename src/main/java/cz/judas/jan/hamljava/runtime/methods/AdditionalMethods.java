package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.*;

import java.util.Collection;
import java.util.Map;

public class AdditionalMethods {
    public static final AdditionalMethods EMPTY = new AdditionalMethods(ImmutableMap.of());

    private final Map<Class<?>, Collection<AdditionalMethod<?>>> additionalMethods;

    public AdditionalMethods(Map<Class<?>, ? extends Collection<AdditionalMethod<?>>> additionalMethods) {
        this.additionalMethods = ImmutableMap.copyOf(additionalMethods);
    }

    @SuppressWarnings("unchecked") // wildcard removal
    public Iterable<AdditionalMethod<?>> forClass(Class<?> clazz) {
        return Iterables.concat(Maps.filterKeys(additionalMethods, (key) -> key.isAssignableFrom(clazz)).values());
    }
}
