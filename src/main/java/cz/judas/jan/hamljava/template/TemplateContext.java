package cz.judas.jan.hamljava.template;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateContext {
    private final Map<String, Object> fieldValues;
    private final Map<String, Object> localVariables;
    private final UnboundRubyMethod block;

    public TemplateContext(Map<String, ?> fieldValues, UnboundRubyMethod block) {
        this(fieldValues, Collections.emptyMap(), block);
    }

    private TemplateContext(Map<String, ?> fieldValues, Map<String, Object> localVariables, UnboundRubyMethod block) {
        this.fieldValues = nullSafeCopy(fieldValues);
        this.localVariables = nullSafeCopy(localVariables);
        this.block = block;
    }

    public Object getField(String name) {
        return MoreObjects.firstNonNull(fieldValues.get(name), RubyConstants.NIL);
    }

    public Object getVariable(String name, AdditionalFunctions functions, HtmlOutput htmlOutput) {
        Object value = localVariables.get(name);
        if (value == null) {
            return callNoArgFunction(functions.withName(name), htmlOutput);
        }
        return value;
    }

    public boolean hasVariable(String name) {
        return localVariables.containsKey(name);
    }

    public Object invokeBlock(HtmlOutput htmlOutput) {
        return callNoArgFunction(block, htmlOutput);
    }

    public TemplateContext withLocalVariables(Map<String, ?> localVariables) {
        Map<String, Object> newScope = new HashMap<>(this.localVariables);
        newScope.putAll(localVariables);
        return new TemplateContext(fieldValues, ImmutableMap.copyOf(newScope), UnboundRubyMethod.EMPTY_BLOCK);
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

    private Object callNoArgFunction(UnboundRubyMethod function, HtmlOutput htmlOutput) {
        return function.invoke(Collections.emptyList(), UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, withLocalVariables(Collections.emptyMap()));
    }
}
