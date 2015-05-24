package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public class RubySymbolExpression implements RubyExpression {
    private final String value;

    public RubySymbolExpression(String value) {
        this.value = value;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return new RubyObject(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RubySymbolExpression that = (RubySymbolExpression) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RubySymbolExpression{" +
                "value='" + value + '\'' +
                '}';
    }
}
