package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class HamlNodeBlock implements UnboundRubyMethod {
    private final HamlNode expression;

    public HamlNodeBlock(HamlNode expression) {
        this.expression = expression;
    }

    @Override
    public Object invoke(List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        expression.evaluate(htmlOutput, templateContext);
        return RubyConstants.NIL;
    }
}
