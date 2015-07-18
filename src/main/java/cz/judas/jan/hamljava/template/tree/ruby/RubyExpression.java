package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

public interface RubyExpression {
    Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
