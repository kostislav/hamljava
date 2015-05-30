package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;
import java.util.Map;

public interface RubyObject {
    default RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    default RubyObject getProperty(String name, HtmlOutput htmlOutput, TemplateContext templateContext) {
        throw new IllegalArgumentException("Property or method " + name + " does not exist");
    }

    String asString();

    Object asJavaObject();

    @SuppressWarnings("ChainOfInstanceofChecks")
    static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
        } else if (javaObject instanceof Map) {
            return RubyHash.fromJava((Map<?, ?>) javaObject);
        } else if (javaObject instanceof Iterable) {
            return RubyIterable.fromJava((Iterable<?>) javaObject);
        } else if (javaObject instanceof String) {
            return new RubyString((String) javaObject);
        } else if (javaObject instanceof Integer) {
            return new RubyInteger((Integer) javaObject);
        } else {
            return new RubyObjectBase(javaObject);
        }
    }
}
