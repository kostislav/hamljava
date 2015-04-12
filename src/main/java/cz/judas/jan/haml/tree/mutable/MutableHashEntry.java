package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyValue;

public class MutableHashEntry {
    private String name;
    private RubyValue value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RubyValue getValue() {
        return value;
    }

    public void setValue(RubyValue value) {
        this.value = value;
    }
}
