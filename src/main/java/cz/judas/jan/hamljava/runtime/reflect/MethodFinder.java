package cz.judas.jan.hamljava.runtime.reflect;

public class MethodFinder {
    private final MethodCallCreator methodCallCreator;
    private final String name;
    private final int argumentCount;

    public MethodFinder(MethodCallCreator methodCallCreator, String name, int argumentCount) {
        this.methodCallCreator = methodCallCreator;
        this.name = name;
        this.argumentCount = argumentCount;
    }

    public MethodCall findOn(Class<?> targetClass) {
        return methodCallCreator.createFor(targetClass, name, argumentCount);
    }
}
