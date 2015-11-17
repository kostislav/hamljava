package cz.judas.jan.hamljava.runtime.reflect;

public interface MethodFinder {
    MethodCall findOn(Class<?> targetClass);
}
