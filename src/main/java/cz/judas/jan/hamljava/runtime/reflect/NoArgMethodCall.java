package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoArgMethodCall implements PropertyAccess {
    private final Method method;

    public NoArgMethodCall(Method method) {
        this.method = method;
    }

    @Override
    public Object get(Object target, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call no-arg method " + method.getName() + " on " + target);
        }
    }
}
