package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.template.tree.HamlNode;

public class CompiledHamlTemplate {
    private final HamlNode rootNode;

    public CompiledHamlTemplate(HamlNode rootNode) {
        this.rootNode = rootNode;
    }

    public LinkedHamlTemplate link() {
        return new LinkedHamlTemplate(rootNode);
    }
}
