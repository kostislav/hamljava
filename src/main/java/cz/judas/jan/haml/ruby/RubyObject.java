package cz.judas.jan.haml.ruby;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RubyObject {
    private final Object javaObject;

    public RubyObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    public Object callMethod(String name) {
        try {
            try {
                return getFieldValue(javaObject, name);
            } catch (NoSuchFieldException e) {
                try {
                    return callGetter(javaObject, name);
                } catch (NoSuchMethodException e1) {
                    return callMethod(javaObject, name);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for for property " + javaObject + "." + name);
        }
    }

    private Object getFieldValue(Object target, String name) throws NoSuchFieldException, IllegalAccessException {
        Field property = target.getClass().getDeclaredField(name);
        property.setAccessible(true);
        return property.get(target);
    }

    private Object callGetter(Object target, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return callMethod(target, "get" + StringUtils.capitalize(name));
    }

    private Object callMethod(Object target, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = target.getClass().getMethod(name);
        getter.setAccessible(true);
        return getter.invoke(target);
    }
}
