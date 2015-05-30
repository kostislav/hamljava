package cz.judas.jan.haml.ruby.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoArgMethodCall implements PropertyAccess {
    private final Method method;

    public NoArgMethodCall(Method method) {
        this.method = method;
    }

    @Override
    public Object get(Object target) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not call no-arg method " + method.getName() + " on " + target);
        }
    }
}
