package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.runtime.RubyConstants;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
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
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.addUnescaped(content.evaluate(htmlOutput, templateContext));

        return RubyConstants.NIL;
    }
}
