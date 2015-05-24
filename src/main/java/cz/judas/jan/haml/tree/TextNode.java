package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;
import cz.judas.jan.haml.ruby.Nil;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.tree.ruby.RubyExpression;

public class TextNode implements HamlNode {
    private final RubyExpression content;

    public TextNode(RubyExpression content) {
        this.content = content;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, VariableMap variableMap) {
        htmlOutput.addUnescaped(content.evaluate(htmlOutput, variableMap).asString());

        return Nil.INSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextNode textNode = (TextNode) o;

        return content.equals(textNode.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "content='" + content + '\'' +
                '}';
    }
}
