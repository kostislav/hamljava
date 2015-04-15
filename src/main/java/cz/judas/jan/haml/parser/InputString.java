package cz.judas.jan.haml.parser;

import java.util.function.Predicate;

public class InputString {
    private final String input;

    private int currentPosition = 0;

    public InputString(String input) {
        this.input = input;
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
