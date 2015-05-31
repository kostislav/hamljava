package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Collection;
import java.util.List;

public class CompoundStringExpression implements RubyExpression {
    private final List<RubyExpression> parts;

    public CompoundStringExpression(Iterable<? extends RubyExpression> parts) {
        this.parts = ImmutableList.copyOf(parts);
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        StringBuilder stringBuilder = new StringBuilder();
        for (RubyExpression part : parts) {
            stringBuilder.append(part.evaluate(htmlOutput, templateContext));
        }
        return RubyObject.wrap(stringBuilder.toString());
    }

    public static RubyExpression from(Collection<? extends RubyExpression> parts) {
        if(parts.size() == 1) {
            return parts.iterator().next();
        } else {
            return new CompoundStringExpression(parts);
        }
    }
}
