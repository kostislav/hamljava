package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.ruby.methods.IterableEach;
import cz.judas.jan.haml.ruby.reflect.MethodCall;
import cz.judas.jan.haml.ruby.reflect.MethodCallCreator;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.List;

public class MethodCallExpression implements PossibleMethodCall {
    private static final MethodCallCreator METHOD_CALL_CREATOR = new MethodCallCreator(ImmutableMultimap.of(
            Iterable.class, new IterableEach()
    ));

    private final String methodName;
    private final RubyBlock block;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;

    public MethodCallExpression(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments) {
        this(target, methodName, arguments, RubyBlock.EMPTY);
    }

    public MethodCallExpression(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments, RubyBlock block) {
        this.target = target;
        this.methodName = methodName;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        Object evaluatedTarget = RubyObject.unwrap(target.evaluate(htmlOutput, variables));

        MethodCall methodCall = METHOD_CALL_CREATOR.createFor(
                evaluatedTarget.getClass(),
                methodName,
                arguments.size()
        );

        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, variables))
                .transform(RubyObject::unwrap)
                .toList();
        return RubyObject.wrap(methodCall.invoke(evaluatedTarget, evaluatedArgs, block, htmlOutput, variables));
    }

    @Override
    public MethodCallExpression withBlock(RubyBlock block) {
        return new MethodCallExpression(target, methodName, arguments, block);
    }
}
