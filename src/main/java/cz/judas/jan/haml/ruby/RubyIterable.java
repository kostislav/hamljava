package cz.judas.jan.haml.ruby;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;

import java.util.List;

public class RubyIterable extends RubyObjectBase {
    private final Iterable<RubyObject> javaObject;

    public RubyIterable(Iterable<RubyObject> javaObject) {
        super(javaObject);
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (name.equals("each")) {
            for (RubyObject o : javaObject) {
                block.invoke(ImmutableList.of(o), RubyBlock.EMPTY, htmlOutput, templateContext);
            }
            return Nil.INSTANCE;
        } else {
            return super.callMethod(name, arguments, block, htmlOutput, templateContext);
        }
    }

    public static RubyIterable fromJava(Iterable<?> javaObject) {
        return new RubyIterable(FluentIterable.from(javaObject).transform(RubyObject::wrap).toList());
    }
}
