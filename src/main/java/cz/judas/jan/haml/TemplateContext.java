package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class TemplateContext {
    private final Map<String, Object> fieldValues;

    public TemplateContext(Map<String, ?> fieldValues) {
        this.fieldValues = ImmutableMap.copyOf(fieldValues);
    }

    public Object getField(String name) {
        Object value = fieldValues.get(name);
        if(value == null && !fieldValues.containsKey(name)) {
            throw new IllegalArgumentException("Field " + name + " does not exist");
        }
        return value;
    }
}
