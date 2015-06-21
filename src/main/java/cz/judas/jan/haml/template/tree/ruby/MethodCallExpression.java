package cz.judas.jan.haml.template.tree.ruby;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.runtime.methods.IterableEach;
import cz.judas.jan.haml.runtime.reflect.MethodCall;
import cz.judas.jan.haml.runtime.reflect.MethodCallCreator;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString(exclude="cache")
public class MethodCallExpression implements PossibleMethodCall {
    private static final MethodCallCreator METHOD_CALL_CREATOR = new MethodCallCreator(ImmutableMultimap.of(
            Iterable.class, new IterableEach()
    ));

    private final String methodName;
    private final RubyBlock block;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;
    private final LoadingCache<Class<?>, MethodCall> cache;

    public MethodCallExpression(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments, RubyBlock block) {
        this.target = target;
        this.methodName = methodName;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
        cache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(key ->  METHOD_CALL_CREATOR.createFor(key, methodName, this.arguments.size())));
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        Object evaluatedTarget = target.evaluate(htmlOutput, variables);

        MethodCall methodCall = cache.getUnchecked(evaluatedTarget.getClass());

        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, variables))
                .toList();
        return methodCall.invoke(evaluatedTarget, evaluatedArgs, block, htmlOutput, variables);
    }

    @Override
    public MethodCallExpression withBlock(RubyBlock block) {
        return new MethodCallExpression(target, methodName, arguments, block);
    }
}
