package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class HamlNodeBlock implements RubyBlock {
    private final HamlNode expression;

    public HamlNodeBlock(HamlNode expression) {
        this.expression = expression;
    }

    @Override
    public Object invoke(List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        HtmlOutput innerHtmlOutput = htmlOutput.newChild();
        expression.evaluate(innerHtmlOutput, templateContext);
        return innerHtmlOutput.build();
    }
}
