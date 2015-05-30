package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public interface RubyBlock {
    RubyBlock EMPTY = (arguments, block, htmlOutput, variableMap) -> {
        throw new IllegalStateException("Block not present");
    };

    RubyObject invoke(List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext);
}
