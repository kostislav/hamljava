package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.VariableMap;

public class RubyString implements RubyExpression {
    public static final RubyString EMPTY = new RubyString("");

    private final String value;

    public RubyString(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(VariableMap variables) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RubyString that = (RubyString) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RubyString{" +
                "value='" + value + '\'' +
                '}';
    }
}
