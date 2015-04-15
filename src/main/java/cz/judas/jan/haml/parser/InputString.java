package cz.judas.jan.haml.parser;

import java.util.function.Predicate;

public class InputString {
    private final String input;

    private int currentPosition = 0;

    public InputString(String input) {
        this.input = input;
    }

    @Deprecated
    public InputString(String input, int currentPosition) {
        this.input = input;
        this.currentPosition = currentPosition;
    }

    public boolean currentCharIs(CharPredicate predicate) {
        return currentPosition < input.length() && predicate.test(input.charAt(currentPosition));
    }

    public void advance() {
        currentPosition++;
    }

    public int matchingCount(CharPredicate predicate) {
        int originalPosition = currentPosition;
        while(currentCharIs(predicate)) {
            currentPosition++;
        }
        return currentPosition - originalPosition;
    }

    @Deprecated
    public int currentPosition() {
        return currentPosition;
    }

    public boolean startsWith(String prefix) {
        if(input.startsWith(prefix, currentPosition)) {
            currentPosition += prefix.length();
            return true;
        } else {
            return false;
        }
    }

    public int compatibilityMethod(CharPredicate predicate, Predicate<Integer> test) {
        if (tryParse(inputString -> test.test(inputString.matchingCount(predicate)))) {
            return currentPosition;
        } else {
            return -1;
        }
    }

    public boolean tryParse(Predicate<InputString> consumer) {
        int snapshotPosition = currentPosition;
        if(consumer.test(this)) {
            return true;
        } else {
            currentPosition = snapshotPosition;
            return false;
        }
    }
}
