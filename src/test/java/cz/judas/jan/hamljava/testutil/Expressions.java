package cz.judas.jan.hamljava.testutil;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.tree.ruby.*;

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

    public static RubyExpression fieldReference(String name) {
        return new FieldReferenceExpression(name);
    }

    public static RubyExpression negation(RubyExpression original) {
        return new NegationExpression(original);
    }

    public static RubyExpression localVariable(String name) {
        return new FunctionOrVariableExpression(name, AdditionalFunctions.EMPTY);
    }
}
