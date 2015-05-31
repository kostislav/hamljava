package cz.judas.jan.haml.ruby.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import cz.judas.jan.haml.ruby.methods.AdditionalMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class PropertyAccessCreator {
    private final ImmutableMultimap<Class<?>, AdditionalMethod> additionalMethods;

    public PropertyAccessCreator(Multimap<? extends Class<?>, ? extends AdditionalMethod> additionalMethods) {
        this.additionalMethods = ImmutableMultimap.copyOf(additionalMethods);
    }

    public PropertyAccess createFor(String name, Class<?> targetClass) {
        for (Field property : targetClass.getDeclaredFields()) {
            if (property.getName().equals(name) && Modifier.isPublic(property.getModifiers())) {
                property.setAccessible(true);
                return new FieldAccess(property);
            }
        }

        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        for (Method method : targetClass.getMethods()) {
            String methodName = method.getName();
            if(method.getParameterCount() == 0 && (methodName.equals(getterName) || methodName.equals(name)) ){
                method.setAccessible(true);
                return new NoArgMethodCall(method);
            }
        }

        for (Map.Entry<Class<?>, AdditionalMethod> entry : additionalMethods.entries()) {
            if(entry.getKey().isAssignableFrom(targetClass)) {
                return new AdditionalNoArgMethodCall(entry.getValue());
            }
        }

        throw new IllegalArgumentException("Property/method " + name + " not found on " + targetClass);
    }
}
