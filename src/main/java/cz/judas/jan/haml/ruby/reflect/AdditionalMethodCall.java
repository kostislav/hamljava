package cz.judas.jan.haml.ruby.reflect;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;
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
        return method.invoke(
                target,
                FluentIterable.from(args)
                        .transform(RubyObject::wrap)
                        .toList(),
                block,
                htmlOutput,
                templateContext
        );
    }
}
