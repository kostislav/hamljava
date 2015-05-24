package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;

public interface RubyExpression {
    RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variables);
}
