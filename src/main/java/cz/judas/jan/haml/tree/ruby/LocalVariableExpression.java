package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Collections;

public class LocalVariableExpression implements RubyExpression {
    private final String name;

    public LocalVariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        if(name.equals("yield")) {
            return templateContext.getBlock().invoke(Collections.emptyList(), RubyBlock.EMPTY, htmlOutput, templateContext.withLocalVariables(Collections.emptyMap()));
        } else {
            return templateContext.getVariable(name);
        }
    }
}
