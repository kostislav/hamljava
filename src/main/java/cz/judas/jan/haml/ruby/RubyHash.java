package cz.judas.jan.haml.ruby;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RubyHash extends RubyObjectBase {
    private final Map<?, ?> javaObject;

    public RubyHash(Map<?, ?> javaObject) {
        super(javaObject);
        this.javaObject = ImmutableMap.copyOf(javaObject);
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (name.equals("each")) {
            javaObject.forEach((key, value) -> block.invoke(
                    ImmutableList.of(RubyObject.wrap(key), RubyObject.wrap(value)),
                    RubyBlock.EMPTY,
                    htmlOutput,
                    templateContext
            ));
            return Nil.INSTANCE;
        } else {
            return super.callMethod(name, arguments, block, htmlOutput, templateContext);
        }
    }

    public void each(BiConsumer<RubyObject, RubyObject> block) {
        javaObject.forEach((k, v) -> block.accept(RubyObject.wrap(k), RubyObject.wrap(v)));
    }
}
