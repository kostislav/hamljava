package cz.judas.jan.haml.ruby;

public class RubyInteger implements RubyObject {
    private final Integer javaObject;

    public RubyInteger(int javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    @Override
    public String asString() {
        return javaObject.toString();
    }

    @Override
    public Object asJavaObject() {
        return javaObject;
    }
}
