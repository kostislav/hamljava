package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyExpression;
import cz.judas.jan.haml.tree.RubyString;

public class MutableHashEntry {
    private RubyExpression name;
    private RubyExpression value;

    public RubyExpression getName() {
        return name;
    }

    public void setName(String name) {
        this.name = new RubyString(name);
    }

    public void setName(MutableRubyExpression name) {
        this.name = name.getValue();
    }

    public RubyExpression getValue() {
        return value;
    }

    public void setValue(MutableRubyExpression value) {
        this.value = value.getValue();
    }
}
