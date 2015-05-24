package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public interface HamlNode {
    RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variableMap);
}
