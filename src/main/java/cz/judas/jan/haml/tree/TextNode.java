package cz.judas.jan.haml.tree;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.Nil;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
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
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        htmlOutput.addUnescaped(content.evaluate(htmlOutput, templateContext).asString());

        return Nil.INSTANCE;
    }
}
