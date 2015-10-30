package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class AdditionalMethods {
    public static final AdditionalMethods EMPTY = new AdditionalMethods(ImmutableMultimap.of());

    private final Multimap<? extends Class<?>, ? extends AdditionalMethod<?>> additionalMethods;

    public AdditionalMethods(Multimap<? extends Class<?>, ? extends AdditionalMethod<?>> additionalMethods) {
        this.additionalMethods = ImmutableMultimap.copyOf(additionalMethods);
    }

    @SuppressWarnings("unchecked") // wildcard removal
    public Iterable<AdditionalMethod<?>> forClass(Class<?> clazz) {
        return (Iterable<AdditionalMethod<?>>) Multimaps.filterKeys(additionalMethods, (key) -> key.isAssignableFrom(clazz)).values();
    }
}
