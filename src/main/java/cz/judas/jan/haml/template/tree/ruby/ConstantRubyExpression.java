package cz.judas.jan.haml.template.tree.ruby;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.runtime.RubySymbol;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ConstantRubyExpression implements RubyExpression {
    public static final ConstantRubyExpression TRUE = new ConstantRubyExpression(true);

    private final Object value;

    public ConstantRubyExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return value;
    }

    public static ConstantRubyExpression symbol(String value) {
        return new ConstantRubyExpression(new RubySymbol(value));
    }
}
