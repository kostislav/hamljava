package cz.judas.jan.haml.ruby;

import java.util.List;

public interface RubyObject {
    RubyObject callMethod(String name, List<RubyObject> arguments, RubyBlock block);

    String asString();

    Object asJavaObject();
}
