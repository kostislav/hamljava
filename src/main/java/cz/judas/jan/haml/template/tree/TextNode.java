package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.template.tree.ruby.RubyExpression;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class TextNode implements HamlNode {
    private final RubyExpression content;

    public TextNode(RubyExpression content) {
        this.content = content;
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.add(content.evaluate(htmlOutput, templateContext));
    }
}
