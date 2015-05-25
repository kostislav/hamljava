package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.RubyHash;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.List;

public class RubyHashExpression implements RubyExpression {
    private final List<HashEntry> entries;

    public RubyHashExpression(Iterable<? extends HashEntry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    @Override
    public RubyHash evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        ImmutableMap.Builder<RubyObject, RubyObject> builder = ImmutableMap.builder();
        for (HashEntry entry : entries) {
            builder.put(entry.getKey().evaluate(htmlOutput, variables), entry.getValue().evaluate(htmlOutput, variables));
        }
        return new RubyHash(builder.build());
    }

    public static RubyHashExpression singleEntryHash(RubyExpression key, RubyExpression value) {
        return new RubyHashExpression(ImmutableList.of(new HashEntry(key, value)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RubyHashExpression that = (RubyHashExpression) o;

        return entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "RubyHashExpression{" +
                "entries=" + entries +
                '}';
    }
}
