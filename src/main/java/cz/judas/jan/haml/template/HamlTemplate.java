package cz.judas.jan.haml.template;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.template.tree.HamlNode;
import cz.judas.jan.haml.template.tree.HamlNodeBlock;

import java.util.Map;

public class HamlTemplate {
    private final HamlNode rootNode;

    public HamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public String evaluate(Map<String, ?> fieldValues) {
        return evaluate(true, fieldValues, RubyBlock.EMPTY);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues) {
        return evaluate(escapeByDefault, fieldValues, RubyBlock.EMPTY);
    }

    public String evaluate(Map<String, ?> fieldValues, HamlTemplate innerTemplate) {
        return evaluate(true, fieldValues, innerTemplate);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, HamlTemplate innerTemplate) {
        return evaluate(escapeByDefault, fieldValues, new HamlNodeBlock(innerTemplate.rootNode));
    }

    private String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, RubyBlock block) {
        HtmlOutput htmlOutput = new HtmlOutput(escapeByDefault);
        rootNode.evaluate(
                htmlOutput,
                new TemplateContext(
                        fieldValues,
                        block
                )
        );
        return htmlOutput.build();
    }
}
