package cz.judas.jan.haml.ruby;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RubyObjectBase implements RubyObject {
    private final Object javaObject;

    public RubyObjectBase(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
