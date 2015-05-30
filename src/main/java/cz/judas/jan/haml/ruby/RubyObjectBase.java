package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import cz.judas.jan.haml.ruby.reflect.MethodCall;
import cz.judas.jan.haml.ruby.reflect.MethodCallCreator;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class RubyObjectBase implements RubyObject {
    private static final MethodCallCreator METHOD_CALL_CREATOR = new MethodCallCreator();

    private final Object javaObject;

    public RubyObjectBase(Object javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        MethodCall methodCall = METHOD_CALL_CREATOR.createFor(
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
