package cz.judas.jan.haml.tokens.terminal;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

public class ExactTextToken implements Token<Object> {
    private final String content;

    public ExactTextToken(String content) {
        this.content = content;
    }

    @Override
    public int tryEat(String line, int position, Object parsingResult) throws ParseException {
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
