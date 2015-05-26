package cz.judas.jan.haml.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Alternatives<T> {
    private final List<Alternative<?, T>> alternatives = new ArrayList<>();

    public static <I, O> Alternatives<O> either(I value, Function<I, O> transform) {
        Alternatives<O> alternatives = new Alternatives<>();
        return alternatives.or(value, transform);
    }

    public <I> Alternatives<T> or(I value, Function<I, T> transform) {
        alternatives.add(new Alternative<>(value, transform));
        return this;
    }

    public T orException() {
        for (Alternative<?, T> alternative : alternatives) {
            if (alternative.value != null) {
                return alternative.value();
            }
        }
        throw new IllegalArgumentException("No alternative found");
    }

    private static class Alternative<I, O> {
        private final I value;
        private final Function<I, O> transform;

        private Alternative(I value, Function<I, O> transform) {
            this.value = value;
            this.transform = transform;
        }

        public O value() {
            return transform.apply(value);
        }
    }
}
