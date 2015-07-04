package cz.judas.jan.haml.runtime;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Nil {
    public static final Nil INSTANCE = new Nil();

    @Override
    public String toString() {
        return "";
    }
}
