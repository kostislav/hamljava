package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;

public class NewLineToken<T> implements Token<T> {
    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        if (line.length() == position || line.charAt(position) != '\n') {
            return -1;
        } else {
            return position + 1;
        }
    }
}
