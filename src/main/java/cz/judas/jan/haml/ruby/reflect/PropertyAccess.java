package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface PropertyAccess {
    Object get(Object target, HtmlOutput htmlOutput, TemplateContext templateContext);
}
