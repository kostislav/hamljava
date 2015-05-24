package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.VariableMap;

import java.util.List;

public class RubyInteger implements RubyObject {
    private final Integer javaObject;

    public RubyInteger(int javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, VariableMap variableMap) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
