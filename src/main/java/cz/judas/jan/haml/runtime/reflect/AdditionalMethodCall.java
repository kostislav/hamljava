package cz.judas.jan.haml.runtime.reflect;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.runtime.methods.AdditionalMethod;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class AdditionalMethodCall implements MethodCall {
    private final AdditionalMethod<Object> method;

    @SuppressWarnings("unchecked")
    public AdditionalMethodCall(AdditionalMethod<?> method) {
        this.method = (AdditionalMethod<Object>) method;
    }

    @Override
    public Object invoke(Object target, List<?> args, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return method.invoke(target, args, block, htmlOutput, templateContext);
    }
}
