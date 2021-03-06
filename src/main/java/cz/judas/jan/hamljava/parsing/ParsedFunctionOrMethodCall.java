package cz.judas.jan.hamljava.parsing;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.runtime.reflect.CachingMethodFinder;
import cz.judas.jan.hamljava.runtime.reflect.MethodCallCreator;
import cz.judas.jan.hamljava.runtime.reflect.ResolvingMethodFinder;
import cz.judas.jan.hamljava.template.tree.ruby.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ParsedFunctionOrMethodCall {
    private final AdditionalFunctions additionalFunctions;
    private final MethodCallCreator methodCallCreator;
    private final Optional<RubyExpression> target;
    private final String name;
    private final Optional<List<? extends RubyExpression>> arguments;
    private final Optional<UnboundRubyMethod> block;

    public ParsedFunctionOrMethodCall(
            AdditionalFunctions additionalFunctions,
            MethodCallCreator methodCallCreator,
            Optional<RubyExpression> target,
            String name,
            Optional<List<? extends RubyExpression>> arguments,
            Optional<UnboundRubyMethod> block
    ) {
        this.additionalFunctions = additionalFunctions;
        this.methodCallCreator = methodCallCreator;
        this.target = target;
        this.name = name;
        this.arguments = arguments;
        this.block = block;
    }

    public ParsedFunctionOrMethodCall withBlock(UnboundRubyMethod block) {
        return new ParsedFunctionOrMethodCall(additionalFunctions, methodCallCreator, target, name, arguments, Optional.of(block));
    }

    public RubyExpression materialize() {
        if(target.isPresent()) {
            if(isCall()) {
                List<? extends RubyExpression> arguments = resolveArguments();
                return new MethodCallExpression(
                        target.get(),
                        new CachingMethodFinder(
                            new ResolvingMethodFinder(methodCallCreator, name, arguments.size())
                        ),
                        arguments,
                        resolveBlock()
                );
            } else {
                return new PropertyAccessExpression(target.get(), name);
            }
        } else {
            if(isCall()) {
                return new FunctionCallExpression(name, additionalFunctions, resolveArguments(), resolveBlock());
            } else {
                return new FunctionOrVariableExpression(name, additionalFunctions);
            }
        }
    }

    private boolean isCall() {
        return block.isPresent() || arguments.isPresent();
    }

    private List<? extends RubyExpression> resolveArguments() {
        return arguments.orElse(Collections.emptyList());
    }

    private UnboundRubyMethod resolveBlock() {
        return block.orElse(UnboundRubyMethod.EMPTY_BLOCK);
    }
}
