package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyExpression;

public class MutableHashEntry {
    private String name;
    private RubyExpression value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RubyExpression getValue() {
        return value;
    }

    public void setValue(MutableRubyExpression value) {
        this.value = value.getValue();
    }
}
