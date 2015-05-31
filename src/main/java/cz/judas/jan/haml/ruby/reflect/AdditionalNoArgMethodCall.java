package cz.judas.jan.haml.ruby.reflect;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Collections;

public class AdditionalNoArgMethodCall implements PropertyAccess {
    private final AdditionalMethod<Object> method;

    @SuppressWarnings("unchecked")
    public AdditionalNoArgMethodCall(AdditionalMethod<?> method) {
        this.method = (AdditionalMethod<Object>)method;
    }

    @Override
    public Object get(Object target, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return method.invoke(target, Collections.emptyList(), block, htmlOutput, templateContext);
    }
}
