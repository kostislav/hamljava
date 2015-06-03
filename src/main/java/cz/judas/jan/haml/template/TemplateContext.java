package cz.judas.jan.haml.template;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.runtime.RubyBlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateContext {
    private final Map<String, Object> fieldValues;
    private final Map<String, Object> localVariables;
    private final RubyBlock block;

    public TemplateContext(Map<String, ?> fieldValues, RubyBlock block) {
        this(fieldValues, Collections.emptyMap(), block);
    }

    private TemplateContext(Map<String, ?> fieldValues, Map<String, Object> localVariables, RubyBlock block) {
        this.fieldValues = ImmutableMap.copyOf(fieldValues);
        this.localVariables = ImmutableMap.copyOf(localVariables);
        this.block = block;
    }

    public Object getField(String name) {
        Object value = fieldValues.get(name);
        if(value == null && !fieldValues.containsKey(name)) {
            throw new IllegalArgumentException("Field " + name + " does not exist");
        }
        return value;
    }

    public Object getVariable(String name) {
        Object value = localVariables.get(name);
        if(value == null) {
            throw new IllegalArgumentException("Variable " + name + " does not exist");
        }
        return value;
    }

    public RubyBlock getBlock() {
        return block;
    }

    public TemplateContext withLocalVariables(Map<String, ?> localVariables) {
        Map<String, Object> newScope = new HashMap<>(this.localVariables);
        newScope.putAll(localVariables);
        return new TemplateContext(fieldValues, ImmutableMap.copyOf(newScope), RubyBlock.EMPTY);
    }
}
