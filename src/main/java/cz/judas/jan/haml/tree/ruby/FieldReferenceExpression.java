package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public class FieldReferenceExpression implements RubyExpression {
    private final String name;

    public FieldReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables) {
        return RubyObject.wrap(variables.getField(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldReferenceExpression that = (FieldReferenceExpression) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "FieldReferenceExpression{" +
                "name='" + name + '\'' +
                '}';
    }
}
