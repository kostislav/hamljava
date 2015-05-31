package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public interface PropertyAccess {
    Object get(Object target, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
