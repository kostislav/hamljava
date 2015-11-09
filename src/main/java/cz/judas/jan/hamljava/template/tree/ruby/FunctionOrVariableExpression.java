package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;

@EqualsAndHashCode
@ToString
public class FunctionOrVariableExpression implements PossibleFunctionCall {
    private final String name;
    private final AdditionalFunctions additionalFunctions;

    public FunctionOrVariableExpression(String name, AdditionalFunctions additionalFunctions) {
        this.name = name;
        this.additionalFunctions = additionalFunctions;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        if(name.equals("yield")) {
            return templateContext.getBlock().invoke(Collections.emptyList(), UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, templateContext.withLocalVariables(Collections.emptyMap()));
        } else {
            return templateContext.getVariable(name);
        }
    }

    @Override
    public RubyExpression withBlock(UnboundRubyMethod block) {
        return new FunctionCallExpression(name, additionalFunctions, Collections.emptyList(), block);
    }
}
