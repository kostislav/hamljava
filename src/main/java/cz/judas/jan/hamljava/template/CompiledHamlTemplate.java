package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.tree.HamlNode;

import java.util.Collections;
import java.util.Map;

public class CompiledHamlTemplate {
    private final HamlNode rootNode;

    public CompiledHamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public LinkedHamlTemplate link(Map<String, ? extends UnboundRubyMethod> functions) {
        return new LinkedHamlTemplate(rootNode, functions);
    }

    public LinkedHamlTemplate linkEmpty() {
        return new LinkedHamlTemplate(rootNode, Collections.emptyMap());
    }
}
