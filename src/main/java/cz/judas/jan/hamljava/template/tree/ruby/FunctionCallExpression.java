package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public class FunctionCallExpression implements PossibleFunctionCall {
    private final String name;
    private final UnboundRubyMethod block;
    private final List<RubyExpression> arguments;

    public FunctionCallExpression(String name, Iterable<? extends RubyExpression> arguments, UnboundRubyMethod block) {
        this.name = name;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, templateContext))
                .toList();

        return templateContext.getFunction(name).invoke(evaluatedArgs, block, htmlOutput, templateContext);
    }

    @Override
    public RubyExpression withBlock(UnboundRubyMethod block) {
        return new FunctionCallExpression(name, arguments, block);
    }
}
