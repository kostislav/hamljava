package cz.judas.jan.haml.ruby;

import java.util.Map;
import java.util.function.BiConsumer;

public class RubyHash extends RubyObject {
    private final Map<RubyObject, RubyObject> javaObject;

    public RubyHash(Map<RubyObject, RubyObject> javaObject) {
        super(javaObject);
        this.javaObject = javaObject;
    }

    public void each(BiConsumer<RubyObject, RubyObject> block) {
        javaObject.forEach(block);
    }
}
