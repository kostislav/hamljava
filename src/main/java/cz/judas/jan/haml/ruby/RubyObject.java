package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

import java.util.List;
import java.util.Map;

public interface RubyObject {
    RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, VariableMap variableMap);

    String asString();

    Object asJavaObject();

    @SuppressWarnings("ChainOfInstanceofChecks")
    static RubyObject wrap(Object javaObject) {
        if(javaObject instanceof Map) {
            return RubyHash.fromJava((Map<?, ?>) javaObject);
        } else if(javaObject instanceof String) {
            return new RubyString((String) javaObject);
        } else if(javaObject instanceof Integer) {
            return new RubyInteger((Integer) javaObject);
        } else {
            return new RubyObjectBase(javaObject);
        }
    }
}
