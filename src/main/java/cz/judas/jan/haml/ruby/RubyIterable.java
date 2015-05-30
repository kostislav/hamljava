package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.ruby.methods.IterableEach;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class RubyIterable extends RubyObjectBase {
    private static final IterableEach ITERABLE_EACH = new IterableEach();
    private final Iterable<?> javaObject;

    public RubyIterable(Iterable<?> javaObject) {
        super(javaObject);
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (name.equals("each")) {
            return ITERABLE_EACH.invoke(javaObject, arguments, block, htmlOutput, templateContext);
        } else {
            return super.callMethod(name, arguments, block, htmlOutput, templateContext);
        }
    }
}
