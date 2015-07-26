package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.tree.HamlNode;
import cz.judas.jan.hamljava.template.tree.HamlNodeBlock;

import java.io.StringWriter;
import java.util.Map;

public class CompiledHamlTemplate {
    private final HamlNode rootNode;

    public CompiledHamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public String evaluate(Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions) {
        return evaluate(true, fieldValues, functions, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions) {
        return evaluate(escapeByDefault, fieldValues, functions, UnboundRubyMethod.EMPTY_BLOCK);
    }

    public String evaluate(Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions, CompiledHamlTemplate innerTemplate) {
        return evaluate(true, fieldValues, functions, innerTemplate);
    }

    public String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions, CompiledHamlTemplate innerTemplate) {
        return evaluate(escapeByDefault, fieldValues, functions, new HamlNodeBlock(innerTemplate.rootNode));
    }

    private String evaluate(boolean escapeByDefault, Map<String, ?> fieldValues, Map<String, ? extends UnboundRubyMethod> functions, UnboundRubyMethod block) {
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
