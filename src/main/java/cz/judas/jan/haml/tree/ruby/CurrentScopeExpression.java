package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.ruby.CurrentScope;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

public class CurrentScopeExpression implements RubyExpression {
    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        return new CurrentScope(templateContext);
    }
}
