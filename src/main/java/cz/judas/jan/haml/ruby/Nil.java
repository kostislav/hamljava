package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

import java.util.List;

public class Nil implements RubyObject {
    public static final Nil INSTANCE = new Nil();

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, VariableMap variableMap) {
        return INSTANCE;
    }

    @Override
    public String asString() {
        return "nil";
    }

    @Override
    public Object asJavaObject() {
        return null;
    }
}
