package cz.judas.jan.haml.runtime.reflect;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoArgMethodCall implements PropertyAccess {
    private final Method method;

    public NoArgMethodCall(Method method) {
        this.method = method;
    }

    @Override
    public Object get(Object target, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call no-arg method " + method.getName() + " on " + target);
        }
    }
}
