package cz.judas.jan.haml.ruby;

public class Nil implements RubyObject {
    public static final Nil INSTANCE = new Nil();

    @Override
    public String asString() {
        return "nil";
    }

    @Override
    public Object asJavaObject() {
        return null;
    }
}
