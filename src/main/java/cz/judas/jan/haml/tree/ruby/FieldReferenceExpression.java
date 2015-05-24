package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.ruby.RubyObjectBase;

public class FieldReferenceExpression implements RubyExpression {
    private final String name;

    public FieldReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return new RubyObjectBase(variables.get(name));
    }
}
