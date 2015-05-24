package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

public interface RubyExpression {
    Object evaluate(HtmlOutput htmlOutput, VariableMap variables);

    default RubyObject evaluateAsRuby(HtmlOutput htmlOutput, VariableMap variables) {
        return new RubyObject(evaluate(htmlOutput, variables));
    }
}
