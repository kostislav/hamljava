package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.ruby.reflect.PropertyAccessCreator;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class RubyObjectBase implements RubyObject {
    private final PropertyAccessCreator propertyAccessCreator = new PropertyAccessCreator();

    private final Object javaObject;

    public RubyObjectBase(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject getProperty(String name, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return RubyObject.wrap(propertyAccessCreator.createFor(name, javaObject.getClass()).get(javaObject));
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

    private Object callMethod(Object target, String name, List<RubyObject> arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == arguments.size()) {
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
