package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;

public interface RubyExpression {
    Object evaluate(VariableMap variables);
}
