package cz.judas.jan.hamljava;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.CompiledHamlTemplate;
import cz.judas.jan.hamljava.template.LinkedHamlTemplate;

import java.util.Map;

public class HamlTemplateLinker {
    private final Map<String, ? extends UnboundRubyMethod> functions;

    public HamlTemplateLinker(Map<String, ? extends UnboundRubyMethod> functions) {
        this.functions = ImmutableMap.copyOf(functions);
    }

    public LinkedHamlTemplate link(CompiledHamlTemplate compiledTemplate) {
        return compiledTemplate.link(functions);
    }
}
