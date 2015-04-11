package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

public class ExactTextToken<T> implements Token<T> {
    private final String content;

    public ExactTextToken(String content) {
        this.content = content;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int length = content.length();
        for (int i = 0; i < length; i++) {
            int linePosition = position + i;
            if(linePosition == line.length() || content.charAt(i) != line.charAt(linePosition)) {
                return -1;
            }
        }
        return position + length;
    }
}
