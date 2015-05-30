package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
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
    public RubyObject getProperty(String name) {
        return RubyObject.wrap(getPropertyOrCallNoArgMethod(name));
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        try {
            return RubyObject.wrap(callMethod(javaObject, name, arguments));
        } catch (Exception e) {
            throw new RuntimeException("Could not call method " + name + " on " + javaObject);
        }
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }

    private Object getPropertyOrCallNoArgMethod(String name) {
        try {
            for (Field property : javaObject.getClass().getDeclaredFields()) {
                if (property.getName().equals(name) && Modifier.isPublic(property.getModifiers())) {
                    property.setAccessible(true);
                    return property.get(javaObject);
                }
            }
            try {
                return callGetter(javaObject, name);
            } catch (NoSuchMethodException | IllegalAccessException e1) {
                return callMethod(javaObject, name, Collections.emptyList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get value for property " + javaObject + "." + name, e);
        }
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
