package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.Nil;

public class Rubyisms {
    public static boolean isFalsey(Object value) {
        return value == null || value instanceof Nil || Boolean.FALSE.equals(value);
    }
}
