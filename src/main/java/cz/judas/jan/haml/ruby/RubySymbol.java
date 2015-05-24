package cz.judas.jan.haml.ruby;

public class RubySymbol implements RubyObject {
    private final String javaObject;

    public RubySymbol(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RubySymbol that = (RubySymbol) o;

        return javaObject.equals(that.javaObject);
    }

    @Override
    public int hashCode() {
        return javaObject.hashCode();
    }
}
