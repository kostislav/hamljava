package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.RubyObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class FieldReferenceExpression implements RubyExpression {
    private final String name;

    public FieldReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        return RubyObject.wrap(variables.getField(name));
    }
}
