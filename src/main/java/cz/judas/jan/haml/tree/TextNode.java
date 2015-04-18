package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.VariableMap;

public class TextNode implements Node {
    private final RubyExpression content;

    public TextNode(RubyExpression content) {
        this.content = content;
    }

    @Override
    public void appendTo(StringBuilder stringBuilder, VariableMap variableMap) {
        stringBuilder.append(content.evaluate(variableMap));
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
