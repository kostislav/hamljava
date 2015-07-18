package cz.judas.jan.hamljava.runtime.methods;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public interface AdditionalMethod<T> {
    Object invoke(T target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
