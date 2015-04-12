package cz.judas.jan.haml.tree.mutable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MutableHash {
    private final Map<String, String> values = new LinkedHashMap<>();

    public void addKeyValuePair(MutableHashEntry attribute) {
        values.put(attribute.getName(), attribute.getValue());
    }

    public void forEach(BiConsumer<String, String> consumer) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
