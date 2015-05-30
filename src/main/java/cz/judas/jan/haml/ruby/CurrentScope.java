package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class CurrentScope implements RubyObject {
    private final TemplateContext scope;

    public CurrentScope(TemplateContext scope) {
        this.scope = scope;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        throw new IllegalArgumentException("Global methods not yet supported");
    }

    @Override
    public RubyObject getProperty(String name) {
        return scope.getVariable(name);
    }

    @Override
    public String asString() {
        throw new UnsupportedOperationException("Cannot call asString on scope object");
    }

    @Override
    public Object asJavaObject() {
        throw new UnsupportedOperationException("Cannot call asJavaObject on scope object");
    }
}
