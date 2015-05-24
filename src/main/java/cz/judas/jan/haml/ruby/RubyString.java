package cz.judas.jan.haml.ruby;

public class RubyString implements RubyObject {
    public static RubyString EMPTY = new RubyString("");

    private final String javaObject;

    public RubyString(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public RubyObject callMethod(String name) {
        throw new IllegalArgumentException("Method " + name + " does not exist");
    }

    @Override
    public String toString() {
        return javaObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RubyString that = (RubyString) o;

        return javaObject.equals(that.javaObject);
    }

    @Override
    public int hashCode() {
        return javaObject.hashCode();
    }
}
