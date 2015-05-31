package cz.judas.jan.haml.template;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.tree.HamlNode;
import cz.judas.jan.haml.tree.HamlNodeBlock;

import java.util.Map;

public class HamlTemplate {
    private final HamlNode rootNode;

    public HamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public String evaluate(Map<String, ?> fieldValues) {
        return evaluate(fieldValues, RubyBlock.EMPTY);
    }

    public String evaluate(Map<String, ?> fieldValues, HamlTemplate innerTemplate) {
        return evaluate(fieldValues, new HamlNodeBlock(innerTemplate.rootNode));
    }

    private String evaluate(Map<String, ?> fieldValues, RubyBlock block) {
        HtmlOutput htmlOutput = new HtmlOutput(false); // TODO make configurable
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
