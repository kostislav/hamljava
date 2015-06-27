package cz.judas.jan.haml.template.tree.ruby;

import cz.judas.jan.haml.runtime.RubyConstants;
import cz.judas.jan.haml.runtime.RubySymbol;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ConstantRubyExpression implements RubyExpression {
    public static final ConstantRubyExpression EMPTY_STRING = new ConstantRubyExpression(RubyConstants.EMPTY_STRING);

    private final Object value;

    public ConstantRubyExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return value;
    }

    // TODO unnecessary
    public static ConstantRubyExpression string(String value) {
        return new ConstantRubyExpression(value);
    }

    public static ConstantRubyExpression symbol(String value) {
        return new ConstantRubyExpression(new RubySymbol(value));
    }

    public static ConstantRubyExpression integer(int value) {
        return new ConstantRubyExpression(value);
    }
}