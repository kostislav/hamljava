package cz.judas.jan.haml.template;

import cz.judas.jan.haml.tree.RootNode;

import java.util.Map;

public class HamlTemplate {
    private final RootNode rootNode;

    public HamlTemplate(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public String evaluateFor(Map<String, ?> fieldValues) {
        HtmlOutput htmlOutput = new HtmlOutput();
        rootNode.evaluate(htmlOutput, new TemplateContext(fieldValues));
        return htmlOutput.build();
    }
}
