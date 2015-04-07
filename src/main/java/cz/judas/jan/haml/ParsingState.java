package cz.judas.jan.haml;

import com.google.common.collect.ImmutableSet;

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
        this.followingStates = ImmutableSet.copyOf(followingStates);
    }

    public int tryEat(String line, int position, ParsingResult parsingResult) throws ParseException {
        if(line.charAt(position) != leadingChar) {
            return -1;
        }

        int currentPosition = position + 1;
        while(currentPosition < line.length() && validChars.test(line.charAt(currentPosition))) {
            currentPosition++;
        }
        onEnd.accept(parsingResult, line.substring(position + 1, currentPosition));

        return currentPosition;
    }

    public Set<State> getFollowingStates() {
        return followingStates;
    }
}
