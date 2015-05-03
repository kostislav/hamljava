package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;

public class MethodCall implements RubyExpression {
    private final String methodName;
    private final RubyExpression target;

    public MethodCall(RubyExpression target, String methodName) {
        this.target = target;
        this.methodName = methodName;
    }

    @Override
    public Object evaluate(VariableMap variables) {
        return target.evaluateAsRuby(variables).callMethod(methodName);
    }
}
