package cz.judas.jan.haml;

import java.util.function.BiConsumer;

public class LeadingCharToken implements Token<ParsingResult> {
    private final char leadingChar;
    private final CharPredicate validChars;
    private final BiConsumer<ParsingResult, String> onEnd;

    public LeadingCharToken(char leadingChar, CharPredicate validChars, BiConsumer<ParsingResult, String> onEnd) {
        this.leadingChar = leadingChar;
        this.validChars = validChars;
        this.onEnd = onEnd;
    }

    @Override
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
}
