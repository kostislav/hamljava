package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;

public class FieldReference implements RubyExpression {
    private final String name;

    public FieldReference(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(VariableMap variables) {
        return variables.get(name);
    }
}
