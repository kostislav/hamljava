package cz.judas.jan.haml.ruby;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class RubyIterable extends RubyObjectBase {
    private final Iterable<?> javaObject;

    public RubyIterable(Iterable<?> javaObject) {
        super(javaObject);
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (name.equals("each")) {
            for (Object o : javaObject) {
                block.invoke(ImmutableList.of(RubyObject.wrap(o)), RubyBlock.EMPTY, htmlOutput, templateContext);
            }
            return Nil.INSTANCE;
        } else {
            return super.callMethod(name, arguments, block, htmlOutput, templateContext);
        }
    }
}
