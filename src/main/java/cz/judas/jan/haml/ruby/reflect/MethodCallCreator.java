package cz.judas.jan.haml.ruby.reflect;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodCallCreator {
    private final ImmutableMultimap<Class<?>, AdditionalMethod> additionalMethods;

    public MethodCallCreator() {
        this(ImmutableMultimap.of());
    }

    public MethodCallCreator(ImmutableMultimap<? extends Class<?>, ? extends AdditionalMethod> additionalMethods) {
        this.additionalMethods = ImmutableMultimap.copyOf(additionalMethods);
    }

    public MethodCall createFor(Class<?> targetClass, String methodName, int argumentCount) {
        for (Method method : targetClass.getMethods()) {
            if(method.getName().equals(methodName) && method.getParameterCount() == argumentCount) {
                method.setAccessible(true);
                return new ReflectionMethodCall(method);
            }
        }

        for (Map.Entry<Class<?>, AdditionalMethod> entry : additionalMethods.entries()) {
            if(entry.getKey().isAssignableFrom(targetClass)) {
                return new AdditionalMethodCall(entry.getValue());
            }
        }

        throw new IllegalArgumentException("Method with name " + methodName + " and " + argumentCount + " arguments not found on " + targetClass);
    }
}
