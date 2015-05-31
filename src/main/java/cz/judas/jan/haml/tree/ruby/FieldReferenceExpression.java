package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
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
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext variables) {
        return variables.getField(name);
    }
}
