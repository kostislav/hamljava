package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHashExpression;
import cz.judas.jan.haml.tree.ruby.RubyStringExpression;
import cz.judas.jan.haml.tree.ruby.RubySymbolExpression;

@SuppressWarnings("UtilityClass")
public class Expressions {
    public static RubyExpression string(String value) {
        return new RubyStringExpression(value);
    }

    public static RubyExpression symbol(String value) {
        return new RubySymbolExpression(value);
    }

    public static RubyHashExpression hash(RubyExpression key, RubyExpression value) {
        return RubyHashExpression.singleEntryHash(key, value);
    }
}
