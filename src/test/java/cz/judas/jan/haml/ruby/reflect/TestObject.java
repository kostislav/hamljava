package cz.judas.jan.haml.ruby.reflect;

@SuppressWarnings("UnusedDeclaration")
public class TestObject {
    public final int publicField;
    private final int privateField;

    public TestObject(int publicField, int privateField) {
        this.publicField = publicField;
        this.privateField = privateField;
    }

    public int getPrivateField() {
        return privateField + 1;
    }

    public String noArgMethod() {
        return "abc";
    }

    public String methodWithArgs(int value) {
        return "abc" + value;
    }

    public int brokenNoArgMethod() {
        throw new IllegalStateException("Is broken");
    }
}
