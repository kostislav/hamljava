package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;

import java.util.Collections;
import java.util.Map;

public class AdditionalFunctions {
    public static final AdditionalFunctions EMPTY = new AdditionalFunctions(Collections.emptyMap());

    private final Map<String, ? extends UnboundRubyMethod> functions;

    public AdditionalFunctions(Map<String, ? extends UnboundRubyMethod> functions) {
        this.functions = ImmutableMap.copyOf(functions);
    }

    public UnboundRubyMethod withName(String name) {
        UnboundRubyMethod function = functions.get(name);
        if(function == null) {
            throw new IllegalArgumentException("Function " + name + " not found");
        } else {
            return function;
        }
    }
}
