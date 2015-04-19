package cz.judas.jan.haml.parser.tokens.generic;

public interface TriFunction<T1, T2, T3, T> {
    T apply(T1 arg1, T2 arg2, T3 arg3);
}
