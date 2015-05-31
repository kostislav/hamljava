package cz.judas.jan.haml.ruby;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.BiConsumer;

public class RubyHash extends RubyObjectBase {
    private final Map<?, ?> javaObject;

    public RubyHash(Map<?, ?> javaObject) {
        super(javaObject);
        this.javaObject = ImmutableMap.copyOf(javaObject);
    }

    public void each(BiConsumer<RubyObject, RubyObject> block) {
        javaObject.forEach((k, v) -> block.accept(RubyObject.wrap(k), RubyObject.wrap(v)));
    }
}
