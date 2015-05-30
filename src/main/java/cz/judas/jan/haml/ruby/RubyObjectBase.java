package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.ruby.reflect.MethodCall;
import cz.judas.jan.haml.ruby.reflect.MethodCallCreator;
import cz.judas.jan.haml.ruby.reflect.PropertyAccessCreator;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class RubyObjectBase implements RubyObject {
    private final PropertyAccessCreator propertyAccessCreator = new PropertyAccessCreator();
    private final MethodCallCreator methodCallCreator = new MethodCallCreator();

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
        MethodCall methodCall = methodCallCreator.createFor(
                javaObject.getClass(),
                name,
                arguments.size()
        );
        return RubyObject.wrap(
                methodCall.invoke(
                        javaObject,
                        FluentIterable.from(arguments)
                                .transform(RubyObject::asJavaObject)
                                .toList()
                )
        );
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
