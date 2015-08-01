package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;

@EqualsAndHashCode
@ToString
public class FunctionOrVariableExpression implements RubyExpression {
    private final String name;

    public FunctionOrVariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        if(name.equals("yield")) {
            return templateContext.getBlock().invoke(Collections.emptyList(), UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, templateContext.withLocalVariables(Collections.emptyMap()));
        } else {
            return templateContext.getVariable(name);
        }
    }
}
