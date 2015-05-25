package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class RubyString implements RubyObject {
    public static final RubyString EMPTY = new RubyString("");

    private final String javaObject;

    public RubyString(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    @Override
    public String asString() {
        return javaObject;
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
