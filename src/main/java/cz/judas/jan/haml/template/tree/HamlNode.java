package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface HamlNode {
    void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
