package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

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
