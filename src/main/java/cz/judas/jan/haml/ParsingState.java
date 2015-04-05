package cz.judas.jan.haml;

import java.util.Map;
import java.util.function.BiConsumer;

public class ParsingState {
    private final CharPredicate validChars;
    private final BiConsumer<ParsingResult, String> onEnd;
    private final Map<Character, State> transitions;

    public ParsingState(CharPredicate validChars, BiConsumer<ParsingResult, String> onEnd, Map<Character, State> transitions) {
        this.validChars = validChars;
        this.onEnd = onEnd;
        this.transitions = transitions;
    }

    public StateTransition eat(String inputLine, int startPosition, ParsingResult parsingResult) throws ParseException {
        int currentPosition = startPosition;
        while(currentPosition < inputLine.length() && validChars.test(inputLine.charAt(currentPosition))) {
            currentPosition++;
        }
        onEnd.accept(parsingResult, inputLine.substring(startPosition, currentPosition));

        if(currentPosition == inputLine.length()) {
            return new StateTransition(State.END, -1);
        } else {
            return new StateTransition(
                    transition(currentPosition, inputLine.charAt(currentPosition)),
                    currentPosition + 1
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
