package cz.judas.jan.haml.ruby.reflect;

import java.lang.reflect.Method;

public class MethodCallCreator {
    public MethodCall createFor(Class<?> targetClass, String methodName, int argumentCount) {
        for (Method method : targetClass.getMethods()) {
            if(method.getName().equals(methodName) && method.getParameterCount() == argumentCount) {
                method.setAccessible(true);
                return new ReflectionMethodCall(method);
            }
        }

        throw new IllegalArgumentException("Method with name " + methodName + " and " + argumentCount + " arguments not found on " + targetClass);
    }
}
