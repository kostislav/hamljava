package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.reflect.MethodCall;
import cz.judas.jan.hamljava.runtime.reflect.MethodCallCreator;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class MethodCallExpression implements RubyExpression {
    private final MethodCallCreator methodCallCreator;

    private final String methodName;
    private final UnboundRubyMethod block;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;

    public MethodCallExpression(MethodCallCreator methodCallCreator, RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments, UnboundRubyMethod block) {
        this.target = target;
        this.methodName = methodName;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
        this.methodCallCreator = methodCallCreator;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        Object evaluatedTarget = target.evaluate(htmlOutput, variables);

        MethodCall methodCall = methodCallCreator.createFor(evaluatedTarget.getClass(), methodName, arguments.size());

        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, variables))
                .toList();
        return methodCall.invoke(evaluatedTarget, evaluatedArgs, block, htmlOutput, variables);
    }
}
