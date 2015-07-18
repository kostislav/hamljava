package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.template.tree.ruby.RubyExpression;
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
