package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
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
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return code.evaluate(htmlOutput, templateContext);
    }
}
