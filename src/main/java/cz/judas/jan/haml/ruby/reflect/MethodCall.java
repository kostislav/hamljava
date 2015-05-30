package cz.judas.jan.haml.ruby.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MethodCall {
    private final Method method;

    public MethodCall(Method method) {
        this.method = method;
    }

    public Object invoke(Object target, List<?> args) {
        try {
            return method.invoke(target, args.toArray(new Object[args.size()]));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call method " + method.getName() + " on " + target);
        }
    }
}
