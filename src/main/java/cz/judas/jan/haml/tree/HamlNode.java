package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface HamlNode {
    Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
