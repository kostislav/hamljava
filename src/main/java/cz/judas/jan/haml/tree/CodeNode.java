package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.tree.ruby.RubyExpression;

public class CodeNode implements HamlNode {
    private final RubyExpression code;

    public CodeNode(RubyExpression code) {
        this.code = code;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variableMap) {
        return code.evaluate(htmlOutput, variableMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodeNode codeNode = (CodeNode) o;

        return code.equals(codeNode.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "CodeNode{" +
                "code=" + code +
                '}';
    }
}
