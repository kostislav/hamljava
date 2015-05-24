package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

public class RubyStringExpression implements RubyExpression {
    public static final RubyStringExpression EMPTY = new RubyStringExpression("");

    private final String value;

    public RubyStringExpression(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, VariableMap variables) {
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

        RubyStringExpression that = (RubyStringExpression) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RubyStringExpression{" +
                "value='" + value + '\'' +
                '}';
    }
}
