package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.template.tree.ruby.RubyExpression;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class CodeNode implements HamlNode {
    private final RubyExpression code;

    public CodeNode(RubyExpression code) {
        this.code = code;
    }

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        code.evaluate(htmlOutput, templateContext);
    }
}
