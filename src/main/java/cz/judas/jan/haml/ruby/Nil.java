package cz.judas.jan.haml.ruby;

public class Nil extends RubyObject {
    public static Nil INSTANCE = new Nil();

    public Nil() {
        super(null);
    }

    @Override
    public RubyObject callMethod(String name) {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "nil";
    }
}
