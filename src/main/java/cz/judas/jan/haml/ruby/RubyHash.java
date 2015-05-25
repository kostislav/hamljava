package cz.judas.jan.haml.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RubyHash extends RubyObjectBase {
    private final Map<RubyObject, RubyObject> javaObject;

    public RubyHash(Map<RubyObject, RubyObject> javaObject) {
        super(javaObject);
        this.javaObject = ImmutableMap.copyOf(javaObject);
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (name.equals("each")) {
            javaObject.forEach((key, value) -> block.invoke(ImmutableList.of(key, value), RubyBlock.EMPTY, htmlOutput, templateContext));
            return Nil.INSTANCE;
        } else {
            return super.callMethod(name, arguments, block, htmlOutput, templateContext);
        }
    }

    public void each(BiConsumer<RubyObject, RubyObject> block) {
        javaObject.forEach(block);
    }

    public static RubyHash fromJava(Map<?, ?> javaMap) {
        ImmutableMap.Builder<RubyObject, RubyObject> builder = ImmutableMap.builder();
        javaMap.forEach((key, value) -> builder.put(RubyObject.wrap(key), RubyObject.wrap(value)));
        return new RubyHash(builder.build());
    }
}
