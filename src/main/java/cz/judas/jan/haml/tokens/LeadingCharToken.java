package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;

import java.util.function.BiConsumer;

public class LeadingCharToken implements Token<MutableHtmlNode> {
    private final char leadingChar;
    private final CharPredicate validChars;
    private final BiConsumer<MutableHtmlNode, String> onEnd;

    public LeadingCharToken(char leadingChar, CharPredicate validChars, BiConsumer<MutableHtmlNode, String> onEnd) {
        this.leadingChar = leadingChar;
        this.validChars = validChars;
        this.onEnd = onEnd;
    }

    @Override
    public int tryEat(String line, int position, MutableHtmlNode mutableHtmlNode) throws ParseException {
        if(line.charAt(position) != leadingChar) {
            return -1;
        }

        int currentPosition = position + 1;
        while(currentPosition < line.length() && validChars.test(line.charAt(currentPosition))) {
            currentPosition++;
        }
        onEnd.accept(mutableHtmlNode, line.substring(position + 1, currentPosition));

        return currentPosition;
    }
}
