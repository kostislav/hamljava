package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public interface MethodCall {
    Object invoke(Object target, List<?> args, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
