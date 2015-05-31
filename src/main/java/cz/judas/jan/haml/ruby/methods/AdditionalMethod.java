package cz.judas.jan.haml.ruby.methods;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public interface AdditionalMethod<T> {
    Object invoke(T target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
