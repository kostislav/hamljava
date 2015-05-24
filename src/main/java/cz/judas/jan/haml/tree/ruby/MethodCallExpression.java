package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.List;

public class MethodCallExpression implements RubyExpression {
    private final String methodName;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;

    public MethodCallExpression(RubyExpression target, String methodName, List<RubyExpression> arguments) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return target.evaluate(htmlOutput, variables).callMethod(methodName);
    }
}
