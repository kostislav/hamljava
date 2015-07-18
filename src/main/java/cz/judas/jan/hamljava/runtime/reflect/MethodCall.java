package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public interface MethodCall {
    Object invoke(Object target, List<?> args, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
