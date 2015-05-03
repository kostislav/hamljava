package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.VariableMap;

import java.util.List;
import java.util.Map;

public class RubyHash implements RubyExpression {
    private final List<HashEntry> entries;

    public RubyHash(Iterable<? extends HashEntry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    @Override
    public Map<Object, Object> evaluate(VariableMap variables) {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        for (HashEntry entry : entries) {
            builder.put(entry.getKey().evaluate(variables), entry.getValue().evaluate(variables));
        }
        return builder.build();
    }

    public static RubyHash singleEntryHash(RubyExpression key, RubyExpression value) {
        return new RubyHash(ImmutableList.of(new HashEntry(key, value)));
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

    @Override
    public String toString() {
        return "RubyHash{" +
                "entries=" + entries +
                '}';
    }
}
