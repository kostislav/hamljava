package cz.judas.jan.hamljava.template.tree.ruby;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
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
}
