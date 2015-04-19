package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.VariableMap;

import java.util.List;
import java.util.function.BiConsumer;

public class RubyHash implements RubyExpression {
    private final List<HashEntry> entries;

    public RubyHash(Iterable<? extends HashEntry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    public void forEach(BiConsumer<RubyExpression, RubyExpression> consumer) {
        for (HashEntry entry : entries) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object evaluate(VariableMap variables) {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        for (HashEntry entry : entries) {
            builder.put(entry.getKey().evaluate(variables), entry.getValue().evaluate(variables));
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RubyHash rubyHash = (RubyHash) o;

        return entries.equals(rubyHash.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }
}
