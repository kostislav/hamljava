package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.HashEntry;
import cz.judas.jan.haml.tree.RubyExpression;

public class MutableHashEntry {
    private RubyExpression name;
    private RubyExpression value;

    public void setKey(RubyExpression name) {
        this.name = name;
    }

    public void setValue(RubyExpression value) {
        this.value = value;
    }

    public HashEntry toEntry() {
        return new HashEntry(name, value);
    }
}
