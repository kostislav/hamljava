package cz.judas.jan.haml.runtime.reflect;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface PropertyAccess {
    Object get(Object target, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
