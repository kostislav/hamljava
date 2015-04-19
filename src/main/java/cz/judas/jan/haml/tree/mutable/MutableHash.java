package cz.judas.jan.haml.tree.mutable;

import cz.judas.jan.haml.tree.HashEntry;
import cz.judas.jan.haml.tree.RubyHash;

import java.util.ArrayList;
import java.util.List;

public class MutableHash {
    private final List<HashEntry> values = new ArrayList<>();

    public void addKeyValuePair(HashEntry attribute) {
        values.add(attribute);
    }

    public RubyHash toHash() {
        return new RubyHash(values);
    }
}
