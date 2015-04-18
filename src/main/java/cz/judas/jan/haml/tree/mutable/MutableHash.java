package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.RubyExpression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MutableHash {
    private final Map<RubyExpression, RubyExpression> values = new LinkedHashMap<>();

    public void addKeyValuePair(MutableHashEntry attribute) {
        values.put(attribute.getName(), attribute.getValue());
    }

    public void forEach(BiConsumer<RubyExpression, RubyExpression> consumer) {
        for (Map.Entry<RubyExpression, RubyExpression> entry : values.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
