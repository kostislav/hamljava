package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface HamlNode {
    void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
