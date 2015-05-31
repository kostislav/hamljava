package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.ruby.RubyInteger;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.ruby.RubyObjectBase;
import cz.judas.jan.haml.ruby.RubySymbol;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ConstantRubyExpression implements RubyExpression {
    public static final ConstantRubyExpression EMPTY_STRING = new ConstantRubyExpression(RubyObject.EMPTY_STRING);

    private final RubyObject value;

    public ConstantRubyExpression(RubyObject value) {
        this.value = value;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return value;
    }

    public static ConstantRubyExpression string(String value) {
        return new ConstantRubyExpression(new RubyObjectBase(value));
    }

    public static ConstantRubyExpression symbol(String value) {
        return new ConstantRubyExpression(new RubySymbol(value));
    }

    public static ConstantRubyExpression integer(int value) {
        return new ConstantRubyExpression(new RubyInteger(value));
    }
}
