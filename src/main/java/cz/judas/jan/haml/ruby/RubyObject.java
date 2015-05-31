package cz.judas.jan.haml.ruby;

public interface RubyObject {
    String asString();

    Object asJavaObject();

    @SuppressWarnings("ChainOfInstanceofChecks")
    static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
        } else if (javaObject instanceof String) {
            return new RubyString((String) javaObject);
        } else if (javaObject instanceof Integer) {
            return new RubyInteger((Integer) javaObject);
        } else {
            return new RubyObjectBase(javaObject);
        }
    }

    static Object unwrap(Object maybeWrapped) {
        if(maybeWrapped instanceof RubyObject) {
            return ((RubyObject)maybeWrapped).asJavaObject();
        } else {
            return maybeWrapped;
        }
    }
}
