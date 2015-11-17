package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(exclude = "additionalFunctions")
public class FunctionCallExpression implements RubyExpression {
    private final String name;
    private final AdditionalFunctions additionalFunctions;
    private final UnboundRubyMethod block;
    private final List<RubyExpression> arguments;

    public FunctionCallExpression(String name, AdditionalFunctions additionalFunctions, Iterable<? extends RubyExpression> arguments, UnboundRubyMethod block) {
        this.name = name;
        this.additionalFunctions = additionalFunctions;
        this.block = block;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        List<Object> evaluatedArgs = FluentIterable.from(arguments)
                .transform(arg -> arg.evaluate(htmlOutput, templateContext))
                .toList();

        return additionalFunctions.withName(name).invoke(evaluatedArgs, block, htmlOutput, templateContext);
    }
}
