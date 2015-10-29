package cz.judas.jan.hamljava.template;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.tree.HamlNode;
import cz.judas.jan.hamljava.template.tree.HamlNodeBlock;

import java.io.StringWriter;
import java.util.Map;

public class LinkedHamlTemplate {
    private final HamlNode rootNode;
    private final Map<String, ? extends UnboundRubyMethod> functions;

    public LinkedHamlTemplate(HamlNode rootNode, Map<String, ? extends UnboundRubyMethod> functions) {
        this.rootNode = rootNode;
        this.functions = ImmutableMap.copyOf(functions);
    }

    public String evaluate(Map<String, ?> fieldValues) {
        return evaluate(true, fieldValues, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues) {
        return evaluate(escapeByDefault, fieldValues, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(Map<String, ?> fieldValues, LinkedHamlTemplate innerTemplate) {
        return evaluate(true, fieldValues, innerTemplate);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, LinkedHamlTemplate innerTemplate) {
        return evaluate(escapeByDefault, fieldValues, new HamlNodeBlock(innerTemplate.rootNode));
    }

    private String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, UnboundRubyMethod block) {
        StringWriter writer = new StringWriter();
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(writer, escapeByDefault);
        rootNode.evaluate(
                htmlOutput,
                new TemplateContext(
                        fieldValues,
                        functions,
                        block
                )
        );
        return writer.toString();
    }
}
