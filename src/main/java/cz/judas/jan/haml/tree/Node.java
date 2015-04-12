package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.VariableMap;

public interface Node {
    void appendTo(StringBuilder stringBuilder, VariableMap variableMap);
}
