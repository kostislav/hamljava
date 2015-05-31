package cz.judas.jan.haml.ruby;

public interface RubyObject {
    RubyObject EMPTY_STRING = new RubyObjectBase("");

    String asString();

    Object asJavaObject();

    @SuppressWarnings("ChainOfInstanceofChecks")
    static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
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
