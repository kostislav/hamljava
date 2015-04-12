package cz.judas.jan.haml.tree;

public class StringRubyValue implements RubyValue {
    private final String value;

    public StringRubyValue(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StringRubyValue that = (StringRubyValue) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "StringRubyValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
