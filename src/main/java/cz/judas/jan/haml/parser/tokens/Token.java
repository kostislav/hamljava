package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.ParseException;

public interface Token<T> {
    int tryEat(String line, int position, T parsingResult) throws ParseException;
}
