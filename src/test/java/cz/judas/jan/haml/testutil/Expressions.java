package cz.judas.jan.haml.testutil;

import cz.judas.jan.haml.template.tree.ruby.ConstantRubyExpression;
import cz.judas.jan.haml.template.tree.ruby.RubyExpression;
import cz.judas.jan.haml.template.tree.ruby.RubyHashExpression;

@SuppressWarnings("UtilityClass")
public class Expressions {
    public static RubyExpression string(String value) {
        return ConstantRubyExpression.string(value);
    }

    public static RubyExpression bool(boolean value) {
        return new ConstantRubyExpression(value);
    }

    public static RubyExpression symbol(String value) {
        return ConstantRubyExpression.symbol(value);
    }

    public static RubyHashExpression hash(RubyExpression key, RubyExpression value) {
        return RubyHashExpression.singleEntryHash(key, value);
    }
}
