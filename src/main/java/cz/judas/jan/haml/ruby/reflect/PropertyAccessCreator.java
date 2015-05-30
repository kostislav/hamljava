package cz.judas.jan.haml.ruby.reflect;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PropertyAccessCreator {
    public PropertyAccess createFor(String name, Class<?> targetClass) {
        for (Field property : targetClass.getDeclaredFields()) {
            if (property.getName().equals(name) && Modifier.isPublic(property.getModifiers())) {
                property.setAccessible(true);
                return new FieldAccess(property);
            }
        }

        String getterName = "get" + StringUtils.capitalize(name);
        for (Method method : targetClass.getMethods()) {
            String methodName = method.getName();
            if(method.getParameterCount() == 0 && (methodName.equals(getterName) || methodName.equals(name)) ){
                method.setAccessible(true);
                return new NoArgMethodCall(method);
            }
        }

        throw new IllegalArgumentException("Property/method " + name + " not found on " + targetClass);
    }
}
