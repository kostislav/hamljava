package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;

public class TextToken implements Token<MutableHtmlNode> {
    @Override
    public int tryEat(String line, int position, MutableHtmlNode parsingResult) throws ParseException {
        int currentPosition = position;
        while(currentPosition < line.length() && line.charAt(currentPosition) != '\n') {
            currentPosition++;
        }

        if(currentPosition == position) {
            return -1;
        } else {
            parsingResult.setContent(line.substring(position, currentPosition));
            return currentPosition;
        }
    }
}
