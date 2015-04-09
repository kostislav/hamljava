package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;

public class SingleCharToken<T> implements Token<T> {
    private final char c;

    public SingleCharToken(char c) {
        this.c = c;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        if (line.length() == position || line.charAt(position) != c) {
            return -1;
        } else {
            return position + 1;
        }
    }
}
