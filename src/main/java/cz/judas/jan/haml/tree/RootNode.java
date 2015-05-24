package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.VariableMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RootNode implements HamlNode {
    private static final Map<String, String> DOCTYPES = ImmutableMap.of(
            "5", "<!DOCTYPE html>"
    );

    private final Optional<String> doctype;
    private final List<HamlNode> children;

    public RootNode(Optional<String> doctype, Iterable<? extends HamlNode> children) {
        this.children = ImmutableList.copyOf(children);
        this.doctype = doctype;
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, VariableMap variableMap) {
        doctype.ifPresent(doctype -> htmlOutput.addUnescaped(DOCTYPES.get(doctype)).addUnescaped('\n'));

        for (HamlNode child : children) {
            child.evaluate(htmlOutput, variableMap);
        }
    }

    public String toHtmlString(VariableMap variableMap) {
        HtmlOutput htmlOutput = new HtmlOutput();
        evaluate(htmlOutput, variableMap);
        return htmlOutput.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RootNode rootNode = (RootNode) o;

        return children.equals(rootNode.children)
                && doctype.equals(rootNode.doctype);
    }

    @Override
    public int hashCode() {
        int result = doctype.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RootNode{" +
                "doctype=" + doctype +
                ", children=" + children +
                '}';
    }
}
