package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.reflect.MethodCall;
import cz.judas.jan.hamljava.runtime.reflect.MethodFinder;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class MethodCallExpression implements RubyExpression {
    private final RubyExpression target;
    private final MethodFinder methodFinder;
    private final UnboundRubyMethod block;
    private final List<RubyExpression> arguments;

    public MethodCallExpression(RubyExpression target, MethodFinder methodFinder, Iterable<? extends RubyExpression> arguments, UnboundRubyMethod block) {
        this.target = target;
        this.methodFinder = methodFinder;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        Object evaluatedTarget = target.evaluate(htmlOutput, variables);
        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, variables))
                .toList();

        MethodCall methodCall = methodFinder.findOn(evaluatedTarget.getClass());

        return methodCall.invoke(
                evaluatedTarget,
                evaluatedArgs,
                block,
                htmlOutput,
                variables
        );
    }
}
