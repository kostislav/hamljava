package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyExpression;

public class MutableRubyExpression {
    private RubyExpression value;

    public RubyExpression getValue() {
        return value;
    }

    public void setValue(RubyExpression value) {
        this.value = value;
    }
}
