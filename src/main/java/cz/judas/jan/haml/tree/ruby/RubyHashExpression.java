package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.ruby.RubyHash;
import cz.judas.jan.haml.ruby.RubyObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
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
}
