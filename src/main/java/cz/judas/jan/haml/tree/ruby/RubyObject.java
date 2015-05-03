package cz.judas.jan.haml.tree.ruby;

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
                return callGetter(javaObject, name);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for for property " + javaObject + "." + name);
        }
    }

    private Object getFieldValue(Object field, String name) throws NoSuchFieldException, IllegalAccessException {
        Field property = field.getClass().getDeclaredField(name);
        property.setAccessible(true);
        return property.get(field);
    }

    private Object callGetter(Object field, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = field.getClass().getMethod("get" + StringUtils.capitalize(name));
        getter.setAccessible(true);
        return getter.invoke(field);
    }
}
