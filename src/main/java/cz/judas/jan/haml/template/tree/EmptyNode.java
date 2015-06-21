package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class EmptyNode implements HamlNode{
    public static final EmptyNode INSTANCE = new EmptyNode();

    @Override
    public void evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
    }
}
