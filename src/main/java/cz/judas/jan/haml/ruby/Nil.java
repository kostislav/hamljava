package cz.judas.jan.haml.ruby;

public class Nil implements RubyObject {
    public static Nil INSTANCE = new Nil();

    @Override
    public RubyObject callMethod(String name) {
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
