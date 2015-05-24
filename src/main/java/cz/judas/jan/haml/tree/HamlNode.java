package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

public interface HamlNode {
    void evaluate(HtmlOutput htmlOutput, VariableMap variableMap);
}
