package cz.judas.jan.hamljava.parsing;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.runtime.reflect.MethodCallCreator;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;

import java.util.Optional;

public class FunctionalNodeBuilder {
    private final AdditionalFunctions functions;
    private final MethodCallCreator methodCallCreator;

    public FunctionalNodeBuilder(AdditionalFunctions functions, MethodCallCreator methodCallCreator) {
        this.functions = functions;
        this.methodCallCreator = methodCallCreator;
    }

    public ParsedFunctionOrMethodCall methodCall(RubyExpression target, String methodName, Iterable<? extends RubyExpression> arguments) {
        return new ParsedFunctionOrMethodCall(functions, methodCallCreator, Optional.of(target), methodName, Optional.of(ImmutableList.copyOf(arguments)), Optional.empty());
    }

    public ParsedFunctionOrMethodCall functionCall(String methodName, Iterable<? extends RubyExpression> arguments) {
        return new ParsedFunctionOrMethodCall(functions, methodCallCreator, Optional.empty(), methodName, Optional.of(ImmutableList.copyOf(arguments)), Optional.empty());
    }

    public ParsedFunctionOrMethodCall propertyAccess(RubyExpression target, String name) {
        return new ParsedFunctionOrMethodCall(functions, methodCallCreator, Optional.of(target), name, Optional.empty(), Optional.empty());
    }

    public ParsedFunctionOrMethodCall localVariable(String name) {
        return new ParsedFunctionOrMethodCall(functions, methodCallCreator, Optional.empty(), name, Optional.empty(), Optional.empty());
    }
}
