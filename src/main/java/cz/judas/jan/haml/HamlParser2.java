package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.RootNode;

public class HamlParser2 {
    private final HamlTreeBuilder2 treeBuilder = new HamlTreeBuilder2();

    public String process(String haml, VariableMap variableMap) {
        RootNode rootNode = treeBuilder.buildTreeFrom(haml);
        return rootNode.toHtmlString(variableMap);
    }
}
