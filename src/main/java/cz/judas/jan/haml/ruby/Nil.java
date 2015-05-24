package cz.judas.jan.haml.ruby;

import java.util.List;

public class Nil implements RubyObject {
    public static Nil INSTANCE = new Nil();

    @Override
    public RubyObject callMethod(String name, List<RubyObject> arguments) {
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
