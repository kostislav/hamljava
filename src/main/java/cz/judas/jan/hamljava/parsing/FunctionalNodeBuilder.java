package cz.judas.jan.hamljava.parsing;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.tree.ruby.*;

public class FunctionalNodeBuilder {
    private final AdditionalFunctions functions;

    public FunctionalNodeBuilder(AdditionalFunctions functions) {
        this.functions = functions;
    }

    public PossibleFunctionCall methodCall(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments) {
        return new MethodCallExpression(target, methodName, arguments, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public PossibleFunctionCall functionCall(String methodName, Iterable<? extends RubyExpression> arguments) {
        return new FunctionCallExpression(methodName, functions, arguments, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public PossibleFunctionCall propertyAccess(RubyExpression target, String name) {
        return new PropertyAccessExpression(target, name);
    }

    public PossibleFunctionCall localVariable(String name) {
        return new FunctionOrVariableExpression(name, functions);
    }
}
