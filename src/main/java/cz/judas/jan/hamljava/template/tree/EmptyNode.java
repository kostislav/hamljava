package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;
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
