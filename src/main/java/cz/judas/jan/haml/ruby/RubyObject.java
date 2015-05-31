package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RubyObject {
    public static final RubyObject NIL = new RubyObject(new Nil());
    public static final RubyObject EMPTY_STRING = new RubyObject("");

    private final Object javaObject;

    public RubyObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String toString() {
        return javaObject.toString();
    }

    public static RubyObject wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return (RubyObject) javaObject;
        } else {
            return new RubyObject(javaObject);
        }
    }

    public static Object unwrap(Object maybeWrapped) {
        if(maybeWrapped instanceof RubyObject) {
            return ((RubyObject)maybeWrapped).javaObject;
        } else {
            return maybeWrapped;
        }
    }
}
