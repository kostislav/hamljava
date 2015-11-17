package cz.judas.jan.hamljava.testutil;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.tree.ruby.ConstantRubyExpression;
import cz.judas.jan.hamljava.template.tree.ruby.FunctionCallExpression;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;
import cz.judas.jan.hamljava.template.tree.ruby.RubyHashExpression;

import java.util.List;

@SuppressWarnings("UtilityClass")
public class Expressions {
    public static RubyExpression string(String value) {
        return new ConstantRubyExpression(value);
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

    public static FunctionCallExpression functionCall(String name, List<? extends RubyExpression> arguments) {
        return new FunctionCallExpression(name, AdditionalFunctions.EMPTY, arguments, UnboundRubyMethod.EMPTY_BLOCK);
    }
}
