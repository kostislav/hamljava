package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MutableHash {
    private final Map<String, RubyValue> values = new LinkedHashMap<>();

    public void addKeyValuePair(MutableHashEntry attribute) {
        values.put(attribute.getName(), attribute.getValue());
    }

    public void forEach(BiConsumer<String, RubyValue> consumer) {
        for (Map.Entry<String, RubyValue> entry : values.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
