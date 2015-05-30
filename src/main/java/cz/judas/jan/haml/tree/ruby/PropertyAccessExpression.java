package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.Collections;

public class PropertyAccessExpression implements PossibleMethodCall {
    private final RubyExpression target;
    private final String name;

    public PropertyAccessExpression(RubyExpression target, String name) {
        this.target = target;
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return target.evaluate(htmlOutput, templateContext).getProperty(name);
    }

    @Override
    public MethodCallExpression withBlock(RubyBlock block) {
        return new MethodCallExpression(target, name, Collections.emptyList(), block);
    }
}
