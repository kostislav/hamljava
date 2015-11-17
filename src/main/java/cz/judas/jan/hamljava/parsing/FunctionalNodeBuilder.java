package cz.judas.jan.hamljava.parsing;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;

import java.util.Optional;

public class FunctionalNodeBuilder {
    private final AdditionalFunctions functions;

    public FunctionalNodeBuilder(AdditionalFunctions functions) {
        this.functions = functions;
    }

    public ParsedFunctionOrMethodCall methodCall(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments) {
        return new ParsedFunctionOrMethodCall(functions, Optional.of(target), methodName, Optional.of(ImmutableList.copyOf(arguments)), Optional.empty());
    }

    public ParsedFunctionOrMethodCall functionCall(String methodName, Iterable<? extends RubyExpression> arguments) {
        return new ParsedFunctionOrMethodCall(functions, Optional.empty(), methodName, Optional.of(ImmutableList.copyOf(arguments)), Optional.empty());
    }

    public ParsedFunctionOrMethodCall propertyAccess(RubyExpression target, String name) {
        return new ParsedFunctionOrMethodCall(functions, Optional.of(target), name, Optional.empty(), Optional.empty());
    }

    public ParsedFunctionOrMethodCall localVariable(String name) {
        return new ParsedFunctionOrMethodCall(functions, Optional.empty(), name, Optional.empty(), Optional.empty());
    }
}
