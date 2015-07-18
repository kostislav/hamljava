package cz.judas.jan.hamljava.runtime.methods;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public class IterableEach implements AdditionalMethod<Iterable<?>> {
    @Override
    public Object invoke(Iterable<?> target, List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        for (Object o : target) {
            block.invoke(ImmutableList.of(o), UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, templateContext);
        }

        return RubyConstants.NIL;
    }
}
