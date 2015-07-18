package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

public interface HamlNode {
    void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
