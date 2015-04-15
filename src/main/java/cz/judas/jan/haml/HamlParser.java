package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.RootNode;

public class HamlParser {
    private final HamlTreeBuilder treeBuilder = new HamlTreeBuilder();

    public String process(String haml, VariableMap variableMap) {
        RootNode rootNode = treeBuilder.buildTreeFrom(haml);
        return rootNode.toHtmlString(variableMap);
    }
}
