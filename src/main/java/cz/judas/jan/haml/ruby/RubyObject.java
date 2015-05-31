package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RubyObject {
    public static final Object NIL = new Nil();
    public static final Object EMPTY_STRING = "";

    private final Object javaObject;

    public RubyObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String toString() {
        return javaObject.toString();
    }
}
