package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;

public interface Token<T> {
    int tryEat(String line, int position, T parsingResult) throws ParseException;
}
