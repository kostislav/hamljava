package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.runtime.methods.AdditionalMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

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
