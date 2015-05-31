package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;
import java.util.Map;

public interface RubyObject {
    default Object callMethod(String name, List<Object> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    String asString();

    Object asJavaObject();

    @SuppressWarnings("ChainOfInstanceofChecks")
    static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
        } else if (javaObject instanceof Map) {
            return new RubyHash((Map<?, ?>) javaObject);
        } else if (javaObject instanceof String) {
            return new RubyString((String) javaObject);
        } else if (javaObject instanceof Integer) {
            return new RubyInteger((Integer) javaObject);
        } else {
            return new RubyObjectBase(javaObject);
        }
    }
}
