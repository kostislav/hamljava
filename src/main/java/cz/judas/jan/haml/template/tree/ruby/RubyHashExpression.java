package cz.judas.jan.haml.template.tree.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class RubyHashExpression implements RubyExpression {
    private final List<HashEntry> entries;

    public RubyHashExpression(Iterable<? extends HashEntry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    @Override
    public Map<Object, Object> evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        for (HashEntry entry : entries) {
            builder.put(entry.getKey().evaluate(htmlOutput, variables), entry.getValue().evaluate(htmlOutput, variables));
        }
        return builder.build();
    }

    public static RubyHashExpression singleEntryHash(RubyExpression key, RubyExpression value) {
        return new RubyHashExpression(ImmutableList.of(new HashEntry(key, value)));
    }
}
