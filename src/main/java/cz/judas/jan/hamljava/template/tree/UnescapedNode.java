package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class UnescapedNode implements HamlNode {
    private final RubyExpression content;

    public UnescapedNode(RubyExpression content) {
        this.content = content;
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.addUnescaped(content.evaluate(htmlOutput, templateContext));
    }
}
