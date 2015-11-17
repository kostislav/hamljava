package cz.judas.jan.hamljava.runtime.reflect;

public class ResolvingMethodFinder implements MethodFinder {
    private final MethodCallCreator methodCallCreator;
    private final String name;
    private final int argumentCount;

    public ResolvingMethodFinder(MethodCallCreator methodCallCreator, String name, int argumentCount) {
        this.methodCallCreator = methodCallCreator;
        this.name = name;
        this.argumentCount = argumentCount;
    }

    @Override
    public MethodCall findOn(Class<?> targetClass) {
        return methodCallCreator.createFor(targetClass, name, argumentCount);
    }
}
