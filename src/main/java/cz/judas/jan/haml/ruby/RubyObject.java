package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RubyObject {
    public static final RubyObject NIL = new RubyObject(new Nil());
    public static final RubyObject EMPTY_STRING = new RubyObject("");

    private final Object javaObject;

    public RubyObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    public String asString() {
        return javaObject.toString();
    }

    public Object asJavaObject() {
        return javaObject;
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    public static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
        } else {
            return new RubyObject(javaObject);
        }
    }

    public static Object unwrap(Object maybeWrapped) {
        if(maybeWrapped instanceof RubyObject) {
            return ((RubyObject)maybeWrapped).asJavaObject();
        } else {
            return maybeWrapped;
        }
    }
}
