package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class VariableMap {
    private final Map<String, Object> values;

    public VariableMap(Map<String, ?> values) {
        this.values = ImmutableMap.copyOf(values);
    }

    public Object get(String name) {
        return values.get(name);
    }
}
