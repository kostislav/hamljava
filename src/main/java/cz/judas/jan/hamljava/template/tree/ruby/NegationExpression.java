package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class NegationExpression implements RubyExpression{
    private final RubyExpression original;

    public NegationExpression(RubyExpression original) {
        this.original = original;
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return Rubyisms.isFalsey(original.evaluate(htmlOutput, templateContext));
    }
}
