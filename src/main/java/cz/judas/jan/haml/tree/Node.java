package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

public interface Node {
    void evaluate(HtmlOutput htmlOutput, VariableMap variableMap);
}
