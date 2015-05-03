package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;

public class RubySymbol implements RubyExpression {
    private final String value;

    public RubySymbol(String value) {
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

        RubySymbol that = (RubySymbol) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RubySymbol{" +
                "value='" + value + '\'' +
                '}';
    }
}
