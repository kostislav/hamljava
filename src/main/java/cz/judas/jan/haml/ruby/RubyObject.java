package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RubyObject {
    public static final Object NIL = wrap(new Nil());
    public static final Object EMPTY_STRING = wrap("");

    private final Object javaObject;

    public RubyObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String toString() {
        return javaObject.toString();
    }

    public static Object wrap(Object javaObject) {
        if (javaObject instanceof RubyObject) {
            return javaObject;
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
