package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyValue;

public class MutableRubyValue {
    private RubyValue value;

    public RubyValue getValue() {
        return value;
    }

    public void setValue(RubyValue value) {
        this.value = value;
    }
}
