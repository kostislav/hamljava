package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.methods.AdditionalMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalMethods;

import java.lang.reflect.Method;

public class MethodCallCreator {
    private final AdditionalMethods additionalMethods;

    public MethodCallCreator(AdditionalMethods additionalMethods) {
        this.additionalMethods = additionalMethods;
    }

    public MethodCall createFor(Class<?> targetClass, String methodName, int argumentCount) {
        for (Method method : targetClass.getMethods()) {
            if(method.getName().equals(methodName) && method.getParameterCount() == argumentCount) {
                method.setAccessible(true);
                return new ReflectionMethodCall(method);
            }
        }

        for (AdditionalMethod<?> method : additionalMethods.forClass(targetClass)) {
            return new AdditionalMethodCall(method);
        }

        throw new IllegalArgumentException("Method with name " + methodName + " and " + argumentCount + " arguments not found on " + targetClass);
    }
}
