package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.List;

public class Nil implements RubyObject {
    public static final Nil INSTANCE = new Nil();

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return INSTANCE;
    }

    @Override
    public RubyObject getProperty(String name, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return INSTANCE;
    }

    @Override
    public String asString() {
        return "nil";
    }

    @Override
    public Object asJavaObject() {
        return null;
    }
}
