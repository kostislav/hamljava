package cz.judas.jan.haml.ruby.methods;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ruby.Nil;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class IterableEach implements AdditionalMethod<Iterable<?>> {
    @Override
    public RubyObject invoke(Iterable<?> target, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        for (Object o : target) {
            block.invoke(ImmutableList.of(RubyObject.wrap(o)), RubyBlock.EMPTY, htmlOutput, templateContext);
        }

        return Nil.INSTANCE;
    }
}
