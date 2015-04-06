package cz.judas.jan.haml;

import java.util.Set;
import java.util.function.BiConsumer;

public class ParsingState {
    private final char leadingChar;
    private final CharPredicate validChars;
    private final BiConsumer<ParsingResult, String> onEnd;
    private final Set<State> followingStates;

    public ParsingState(char leadingChar, CharPredicate validChars, BiConsumer<ParsingResult, String> onEnd, Set<State> followingStates) {
        this.leadingChar = leadingChar;
        this.validChars = validChars;
        this.onEnd = onEnd;
        this.followingStates = followingStates;
    }

    public boolean canParse(String line, int position) {
        return line.charAt(position) == leadingChar;
    }

    public int eat(String inputLine, int startPosition, ParsingResult parsingResult) throws ParseException {
        int currentPosition = startPosition + 1;
        while(currentPosition < inputLine.length() && validChars.test(inputLine.charAt(currentPosition))) {
            currentPosition++;
        }
        onEnd.accept(parsingResult, inputLine.substring(startPosition + 1, currentPosition));

        return currentPosition;
    }

    public Set<State> getFollowingStates() {
        return followingStates;
    }
}
