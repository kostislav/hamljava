package cz.judas.jan.haml.runtime.methods;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public interface AdditionalMethod<T> {
    Object invoke(T target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
