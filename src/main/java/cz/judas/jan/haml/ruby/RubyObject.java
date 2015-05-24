package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.VariableMap;

import java.util.List;

public interface RubyObject {
    RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, VariableMap variableMap);

    String asString();

    Object asJavaObject();
}
