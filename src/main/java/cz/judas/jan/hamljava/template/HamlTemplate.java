package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.template.tree.HamlNode;
import cz.judas.jan.hamljava.template.tree.HamlNodeBlock;

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
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(escapeByDefault);
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
