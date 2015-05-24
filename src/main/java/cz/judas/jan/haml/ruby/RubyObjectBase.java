package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.VariableMap;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

public class RubyObjectBase implements RubyObject {
    private final Object javaObject;

    public RubyObjectBase(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, VariableMap variableMap) {
        return new RubyObjectBase(callJavaMethod(name, arguments));
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }

    private Object callJavaMethod(String name, List<RubyObject> arguments) {
        try {
            try {
                return getFieldValue(javaObject, name);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                try {
                    return callGetter(javaObject, name);
                } catch (NoSuchMethodException | IllegalAccessException e1) {
                    return callMethod(javaObject, name, arguments);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for for property " + javaObject + "." + name, e);
        }
    }

    private Object getFieldValue(Object target, String name) throws NoSuchFieldException, IllegalAccessException {
        Field property = target.getClass().getDeclaredField(name);
        if (!Modifier.isPublic(property.getModifiers())) {
            throw new IllegalAccessException("Field " + name + " is not public");
        }
        property.setAccessible(true);
        return property.get(target);
    }

    private Object callGetter(Object target, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return callMethod(target, "get" + StringUtils.capitalize(name), Collections.emptyList());
    }

    private Object callMethod(Object target, String name, List<RubyObject> arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Method method : target.getClass().getMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(name) && method.getParameterCount() == arguments.size()) {
                method.setAccessible(true);
                return method.invoke(
                        target,
                        FluentIterable.from(arguments)
                                .transform(RubyObject::asJavaObject)
                                .toArray(Object.class)
                );
            }
        }
        throw new NoSuchMethodException("Method " + name + " not found");
    }
}
