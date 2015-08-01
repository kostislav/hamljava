package cz.judas.jan.hamljava.template;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateContext {
    private final Map<String, Object> fieldValues;
    private final Map<String, Object> localVariables;
    private final UnboundRubyMethod block;
    private final Map<String, UnboundRubyMethod> functions;

    public TemplateContext(Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions, UnboundRubyMethod block) {
        this(fieldValues, functions, Collections.emptyMap(), block);
    }

    private TemplateContext(Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions, Map<String, Object> localVariables, UnboundRubyMethod block) {
        this.functions = ImmutableMap.copyOf(functions);
        this.fieldValues = nullSafeCopy(fieldValues);
        this.localVariables = nullSafeCopy(localVariables);
        this.block = block;
    }

    public Object getField(String name) {
        return MoreObjects.firstNonNull(fieldValues.get(name), RubyConstants.NIL);
    }

    public Object getVariable(String name) {
        Object value = localVariables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable " + name + " does not exist");
        }
        return value;
    }

    public boolean hasVariable(String name) {
        return localVariables.containsKey(name);
    }

    public UnboundRubyMethod getBlock() {
        return block;
    }

    public UnboundRubyMethod getFunction(String name) {
        UnboundRubyMethod function = functions.get(name);
        if(function == null) {
            throw new IllegalArgumentException("Function " + name + " not found");
        } else {
            return function;
        }
    }

    public TemplateContext withLocalVariables(Map<String, ?> localVariables) {
        Map<String, Object> newScope = new HashMap<>(this.localVariables);
        newScope.putAll(localVariables);
        return new TemplateContext(fieldValues, functions, ImmutableMap.copyOf(newScope), UnboundRubyMethod.EMPTY_BLOCK);
    }

    private ImmutableMap<String, Object> nullSafeCopy(Map<String, ?> fieldValues) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        fieldValues.forEach((key, value) -> {
            if (value != null) {
                builder.put(key, value);
            }
        });
        return builder.build();
    }
}
