package cz.judas.jan.haml.ruby;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RubyObjectBase implements RubyObject {
    private final Object javaObject;

    public RubyObjectBase(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name) {
        return new RubyObjectBase(callJavaMethod(name));
    }

    @Override
    public String toString() {
        return javaObject.toString();
    }

    private Object callJavaMethod(String name) {
        try {
            try {
                return getFieldValue(javaObject, name);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                try {
                    return callGetter(javaObject, name);
                } catch (NoSuchMethodException | IllegalAccessException e1) {
                    return callMethod(javaObject, name);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for for property " + javaObject + "." + name);
        }
    }

    private Object getFieldValue(Object target, String name) throws NoSuchFieldException, IllegalAccessException {
        Field property = target.getClass().getDeclaredField(name);
        if(!Modifier.isPublic(property.getModifiers())) {
            throw new IllegalAccessException("Field " + name + " is not public");
        }
        property.setAccessible(true);
        return property.get(target);
    }

    private Object callGetter(Object target, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return callMethod(target, "get" + StringUtils.capitalize(name));
    }

    private Object callMethod(Object target, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = target.getClass().getMethod(name);
        if(!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalAccessException("Method " + name + " is not public");
        }
        method.setAccessible(true);
        return method.invoke(target);
    }
}
