package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public class MethodCallExpression implements RubyExpression {
    private final String methodName;
    private final RubyExpression target;

    public MethodCallExpression(RubyExpression target, String methodName) {
        this.target = target;
        this.methodName = methodName;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return target.evaluate(htmlOutput, variables).callMethod(methodName);
    }
}
