package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RubyString implements RubyObject {
    public static final RubyString EMPTY = new RubyString("");

    private final String javaObject;

    public RubyString(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String asString() {
        return javaObject;
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
