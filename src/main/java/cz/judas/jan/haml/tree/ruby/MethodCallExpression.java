package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.List;

public class MethodCallExpression implements RubyExpression {
    private final String methodName;
    private final RubyBlock block;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;

    public MethodCallExpression(RubyExpression target, String methodName, List<RubyExpression> arguments, RubyBlock block) {
        this.target = target;
        this.methodName = methodName;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        ImmutableList<RubyObject> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, variables))
                .toList();
        return target.evaluate(htmlOutput, variables).callMethod(methodName, evaluatedArgs, block);
    }
}
