package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.tree.HamlNode;
import cz.judas.jan.hamljava.template.tree.HamlNodeBlock;

import java.io.StringWriter;
import java.util.Map;

public class HamlTemplate {
    private final HamlNode rootNode;

    public HamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public String evaluate(Map<String, ?> fieldValues) {
        return evaluate(true, fieldValues, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues) {
        return evaluate(escapeByDefault, fieldValues, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(Map<String, ?> fieldValues, HamlTemplate innerTemplate) {
        return evaluate(true, fieldValues, innerTemplate);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, HamlTemplate innerTemplate) {
        return evaluate(escapeByDefault, fieldValues, new HamlNodeBlock(innerTemplate.rootNode));
    }

    public void writeTo(HtmlOutput htmlOutput, TemplateContext context) {
        rootNode.evaluate(htmlOutput, context);
    }

    private String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, UnboundRubyMethod block) {
        StringWriter writer = new StringWriter();
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(writer, escapeByDefault);
        rootNode.evaluate(
                htmlOutput,
                new TemplateContext(
                        fieldValues,
                        block
                )
        );
        return writer.toString();
    }
}
