package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.VariableMap;

public interface RubyValue {
    Object evaluate(VariableMap variables);
}
