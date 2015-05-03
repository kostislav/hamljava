package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;

public interface RubyExpression {
    Object evaluate(VariableMap variables);

    default RubyObject evaluateAsRuby(VariableMap variables) {
        return new RubyObject(evaluate(variables));
    }
}
