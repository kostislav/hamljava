package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.RubyObject;

public class LocalVariableExpression implements RubyExpression {
    private final String name;

    public LocalVariableExpression(String name) {
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return templateContext.getVariable(name);
    }
}
