package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public class EmptyNode implements HamlNode{
    public static final EmptyNode INSTANCE = new EmptyNode();

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return null;
    }
}
