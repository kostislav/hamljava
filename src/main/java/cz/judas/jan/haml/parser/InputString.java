package cz.judas.jan.haml.parser;

import java.util.Optional;
import java.util.function.Predicate;

public class InputString {
    private final String input;

    private int currentPosition = 0;

    public InputString(String input) {
        this.input = input;
    }

    public InputString(String input, int currentPosition) {
        this.input = input;
        this.currentPosition = currentPosition;
    }

    public boolean currentCharIs(CharPredicate predicate) {
        return currentPosition < input.length() && predicate.test(input.charAt(currentPosition));
    }

    public boolean currentCharIs(char c) {
        return currentPosition < input.length() && input.charAt(currentPosition) == c;
    }

    public boolean advanceIf(char c) {
        if (currentPosition < input.length() && input.charAt(currentPosition) == c) {
            currentPosition++;
            return true;
        } else {
            return false;
        }
    }

    public void advance() {
        currentPosition++;
    }

    public boolean hasMoreChars() {
        return currentPosition < input.length();
    }

    public int matchingCount(CharPredicate predicate) {
        int originalPosition = currentPosition;
        while(currentCharIs(predicate)) {
            currentPosition++;
        }
        return currentPosition - originalPosition;
    }

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

    public Optional<String> tryGetSubstringIf(CharPredicate predicate, Predicate<Integer> test) {
        return tryParseString(inputString -> test.test(inputString.matchingCount(predicate)));
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

    public Optional<String> tryParseString(Predicate<InputString> consumer) {
        int snapshotPosition = currentPosition;
        if(consumer.test(this)) {
            return Optional.of(input.substring(snapshotPosition, currentPosition));
        } else {
            currentPosition = snapshotPosition;
            return Optional.empty();
        }
    }
}
