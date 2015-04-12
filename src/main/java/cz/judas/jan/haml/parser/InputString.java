package cz.judas.jan.haml.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkState;

public class InputString {
    private final String input;
    private final Deque<Snapshot> snaphosts = new ArrayDeque<>();

    private int substringStart = 0;
    private int currentPosition = 0;

    public InputString(String input) {
        this.input = input;
    }

    public char currentChar() {
        return charAt();
    }

    public char nextChar() {
        currentPosition++;
        return charAt();
    }

    private char charAt() {
        if(currentPosition >= input.length()) {
            return 0;
        } else {
            return input.charAt(currentPosition);
        }
    }

    public String unprocessedPart() {
        int tempSubstringStart = substringStart;
        substringStart = currentPosition;
        return input.substring(tempSubstringStart, currentPosition);
    }

    public void takeSnapshot() {
        snaphosts.push(new Snapshot(currentPosition, substringStart));
    }

    public void revert() {
        checkState(!snaphosts.isEmpty(), "No snapshot available");

        Snapshot snapshot = snaphosts.pop();
        currentPosition = snapshot.getCurrentPosition();
        substringStart = snapshot.getSubstringStart();
    }

    private static class Snapshot {
        private final int currentPosition;
        private final int substringStart;

        private Snapshot(int currentPosition, int substringStart) {
            this.currentPosition = currentPosition;
            this.substringStart = substringStart;
        }

        private int getCurrentPosition() {
            return currentPosition;
        }

        private int getSubstringStart() {
            return substringStart;
        }
    }
}
