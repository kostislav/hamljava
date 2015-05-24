package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public class FieldReferenceExpression implements RubyExpression {
    private final String name;

    public FieldReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return new RubyObject(variables.get(name));
    }
}
