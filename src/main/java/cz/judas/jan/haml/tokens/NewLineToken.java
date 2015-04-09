package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;

public class NewLineToken<T> implements Token<T> {
    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        if(line.charAt(position) == '\n') {
            return position + 1;
        } else {
            return -1;
        }
    }
}
