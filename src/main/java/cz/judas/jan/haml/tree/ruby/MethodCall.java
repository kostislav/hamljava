package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.VariableMap;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCall implements RubyExpression {
    private final String fieldName;
    private final String methodName;

    public MethodCall(String fieldName, String methodName) {
        this.fieldName = fieldName;
        this.methodName = methodName;
    }

    @Override
    public Object evaluate(VariableMap variables) {
        Object field = variables.get(fieldName);
        try {
            try {
                return getFieldValue(field);
            } catch (NoSuchFieldException e) {
                return callGetter(field);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for for property " + fieldName + "." + methodName);
        }
    }

    private Object getFieldValue(Object field) throws NoSuchFieldException, IllegalAccessException {
        Field property = field.getClass().getDeclaredField(methodName);
        property.setAccessible(true);
        return property.get(field);
    }

    private Object callGetter(Object field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = field.getClass().getMethod("get" + StringUtils.capitalize(methodName));
        getter.setAccessible(true);
        return getter.invoke(field);
    }
}
