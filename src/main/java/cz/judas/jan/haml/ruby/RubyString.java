package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

import java.util.List;

public class RubyString implements RubyObject {
    public static final RubyString EMPTY = new RubyString("");

    private final String javaObject;

    public RubyString(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, VariableMap variableMap) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    @Override
    public String asString() {
        return javaObject;
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
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

        return javaObject.equals(that.javaObject);
    }

    @Override
    public int hashCode() {
        return javaObject.hashCode();
    }

    @Override
    public String toString() {
        return "RubyString{" +
                "javaObject='" + javaObject + '\'' +
                '}';
    }
}
