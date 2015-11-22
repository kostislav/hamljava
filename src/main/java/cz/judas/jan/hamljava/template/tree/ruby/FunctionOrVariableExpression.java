package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(exclude = "additionalFunctions")
@ToString
public class FunctionOrVariableExpression implements RubyExpression {
    private final String name;
    private final AdditionalFunctions additionalFunctions;

    public FunctionOrVariableExpression(String name, AdditionalFunctions additionalFunctions) {
        this.name = name;
        this.additionalFunctions = additionalFunctions;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        if(name.equals("yield")) {
            return templateContext.invokeBlock(htmlOutput);
        } else {
            return templateContext.getVariable(name, additionalFunctions, htmlOutput);
        }
    }
}
