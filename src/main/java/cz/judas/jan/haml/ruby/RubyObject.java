package cz.judas.jan.haml.ruby;

public interface RubyObject {
    RubyObject callMethod(String name);

    String asString();
}
