package cz.judas.jan.hamljava.runtime;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class RubySymbol {
    private final String javaObject;

    public RubySymbol(String javaObject) {
        this.javaObject = javaObject;
    }

    @Override
    public String toString() {
        return javaObject;
    }
}
