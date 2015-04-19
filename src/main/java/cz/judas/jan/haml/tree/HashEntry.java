package cz.judas.jan.haml.tree;

public class HashEntry {
    private final RubyExpression key;
    private final RubyExpression value;

    public HashEntry(RubyExpression key, RubyExpression value) {
        this.key = key;
        this.value = value;
    }

    public RubyExpression getKey() {
        return key;
    }

    public RubyExpression getValue() {
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

        HashEntry hashEntry = (HashEntry) o;

        return key.equals(hashEntry.key)
                && value.equals(hashEntry.value);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
