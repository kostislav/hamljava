package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.VariableMap;

public class VariableReference implements RubyValue {
    private final String name;

    public VariableReference(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(VariableMap variables) {
        return variables.get(name);
    }
}
