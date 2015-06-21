package cz.judas.jan.haml.template.tree.ruby;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface RubyExpression {
    Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext);
}
