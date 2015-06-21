package cz.judas.jan.haml.runtime.reflect;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public interface MethodCall {
    Object invoke(Object target, List<?> args, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
