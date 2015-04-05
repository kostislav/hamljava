package cz.judas.jan.haml;

import java.util.Map;
import java.util.function.BiConsumer;

public class ParsingState {
    private final char leadingChar;
    private final CharPredicate validChars;
    private final BiConsumer<ParsingResult, String> onEnd;
    private final Map<Character, State> transitions;

    public ParsingState(char leadingChar, CharPredicate validChars, BiConsumer<ParsingResult, String> onEnd, Map<Character, State> transitions) {
        this.leadingChar = leadingChar;
        this.validChars = validChars;
        this.onEnd = onEnd;
        this.transitions = transitions;
    }

    public boolean startsWith(char c) {
        return c == leadingChar;
    }

    public StateTransition eat(String inputLine, int startPosition, ParsingResult parsingResult) throws ParseException {
        int currentPosition = startPosition + 1;
        while(currentPosition < inputLine.length() && validChars.test(inputLine.charAt(currentPosition))) {
            currentPosition++;
        }
        onEnd.accept(parsingResult, inputLine.substring(startPosition + 1, currentPosition));

        if(currentPosition == inputLine.length()) {
            return new StateTransition(State.END, -1);
        } else {
            return new StateTransition(
                    transition(currentPosition, inputLine.charAt(currentPosition)),
                    currentPosition
            );
        }
    }

    private State transition(int currentPosition, char c) throws ParseException {
        State newState = transitions.get(c);
        if (newState == null) {
            throw new ParseException("Could not parse line at position " + currentPosition);
        } else {
            return newState;
        }
    }
}
