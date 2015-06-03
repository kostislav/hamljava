package cz.judas.jan.haml.runtime.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import cz.judas.jan.haml.runtime.methods.AdditionalMethod;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodCallCreator {
    private final Multimap<Class<?>, AdditionalMethod<?>> additionalMethods;

    public MethodCallCreator(Multimap<? extends Class<?>, ? extends AdditionalMethod<?>> additionalMethods) {
        this.additionalMethods = ImmutableMultimap.copyOf(additionalMethods);
    }

    public MethodCall createFor(Class<?> targetClass, String methodName, int argumentCount) {
        for (Method method : targetClass.getMethods()) {
            if(method.getName().equals(methodName) && method.getParameterCount() == argumentCount) {
                method.setAccessible(true);
                return new ReflectionMethodCall(method);
            }
        }

        for (Map.Entry<Class<?>, AdditionalMethod<?>> entry : additionalMethods.entries()) {
            if(entry.getKey().isAssignableFrom(targetClass)) {
                return new AdditionalMethodCall(entry.getValue());
            }
        }

        throw new IllegalArgumentException("Method with name " + methodName + " and " + argumentCount + " arguments not found on " + targetClass);
    }
}
