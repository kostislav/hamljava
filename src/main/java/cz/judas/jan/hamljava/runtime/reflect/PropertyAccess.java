package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

public interface PropertyAccess {
    Object get(Object target, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
