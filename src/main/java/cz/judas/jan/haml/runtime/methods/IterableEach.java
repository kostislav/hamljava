package cz.judas.jan.haml.runtime.methods;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.runtime.RubyConstants;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class IterableEach implements AdditionalMethod<Iterable<?>> {
    @Override
    public Object invoke(Iterable<?> target, List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        for (Object o : target) {
            block.invoke(ImmutableList.of(o), RubyBlock.EMPTY, htmlOutput, templateContext);
        }

        return RubyConstants.NIL;
    }
}
