package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public class EmptyNode implements HamlNode{
    public static final EmptyNode INSTANCE = new EmptyNode();

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
    }
}
