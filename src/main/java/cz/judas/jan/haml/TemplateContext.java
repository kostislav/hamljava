package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateContext {
    private final Map<String, Object> fieldValues;
    private final Map<String, RubyObject> localVariables;

    public TemplateContext(Map<String, ?> fieldValues) {
        this.fieldValues = ImmutableMap.copyOf(fieldValues);
        localVariables = Collections.emptyMap();
    }

    private TemplateContext(Map<String, Object> fieldValues, Map<String, RubyObject> localVariables) {
        this.fieldValues = fieldValues;
        this.localVariables = ImmutableMap.copyOf(localVariables);
    }

    public Object getField(String name) {
        Object value = fieldValues.get(name);
        if(value == null && !fieldValues.containsKey(name)) {
            throw new IllegalArgumentException("Field " + name + " does not exist");
        }
        return value;
    }

    public RubyObject getVariable(String name) {
        RubyObject value = localVariables.get(name);
        if(value == null) {
            throw new IllegalArgumentException("Variable " + name + " does not exist");
        }
        return value;
    }

    public TemplateContext withLocalVariables(Map<String, RubyObject> localVariables) {
        Map<String, RubyObject> newScope = new HashMap<>(this.localVariables);
        newScope.putAll(localVariables);
        return new TemplateContext(fieldValues, ImmutableMap.copyOf(newScope));
    }
}
