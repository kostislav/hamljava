package cz.judas.jan.hamljava.runtime;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public interface UnboundRubyMethod {
    UnboundRubyMethod EMPTY_BLOCK = (arguments, block, htmlOutput, variableMap) -> {
        throw new IllegalStateException("Block not present");
    };

    Object invoke(List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
