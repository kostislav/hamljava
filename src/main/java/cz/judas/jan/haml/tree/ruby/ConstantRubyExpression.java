package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.RubyInteger;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.ruby.RubyString;
import cz.judas.jan.haml.ruby.RubySymbol;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ConstantRubyExpression implements RubyExpression {
    public static final ConstantRubyExpression EMPTY_STRING = new ConstantRubyExpression(RubyString.EMPTY);

    private final RubyObject value;

    public ConstantRubyExpression(RubyObject value) {
        this.value = value;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return value;
    }

    public static ConstantRubyExpression string(String value) {
        return new ConstantRubyExpression(new RubyString(value));
    }

    public static ConstantRubyExpression symbol(String value) {
        return new ConstantRubyExpression(new RubySymbol(value));
    }

    public static ConstantRubyExpression integer(int value) {
        return new ConstantRubyExpression(new RubyInteger(value));
    }
}
