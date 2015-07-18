package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionMethodCall implements MethodCall {
    private final Method method;

    public ReflectionMethodCall(Method method) {
        this.method = method;
    }

    @Override
    public Object invoke(Object target, List<?> args, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        try {
            return method.invoke(target, args.toArray(new Object[args.size()]));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call method " + method.getName() + " on " + target);
        }
    }
}
