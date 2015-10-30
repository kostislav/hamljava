package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalClassMethods;
import cz.judas.jan.hamljava.runtime.methods.AdditionalMethods;
import cz.judas.jan.hamljava.runtime.methods.IterableEach;
import cz.judas.jan.hamljava.runtime.reflect.MethodCall;
import cz.judas.jan.hamljava.runtime.reflect.MethodCallCreator;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString(exclude = "cache")
public class MethodCallExpression implements PossibleFunctionCall {
    private static final MethodCallCreator METHOD_CALL_CREATOR = new MethodCallCreator(new AdditionalMethods(ImmutableSet.of(
            new AdditionalClassMethods<>(Iterable.class, ImmutableMap.of("each", new IterableEach()))
    )));

    private final String methodName;
    private final UnboundRubyMethod block;
    private final RubyExpression target;
    private final List<RubyExpression> arguments;
    private final LoadingCache<Class<?>, MethodCall> cache;

    public MethodCallExpression(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments, UnboundRubyMethod block) {
        this.target = target;
        this.methodName = methodName;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
        cache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(key -> METHOD_CALL_CREATOR.createFor(key, methodName, this.arguments.size())));
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
    public MethodCallExpression withBlock(UnboundRubyMethod block) {
        return new MethodCallExpression(target, methodName, arguments, block);
    }
}
