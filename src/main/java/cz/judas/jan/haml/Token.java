package cz.judas.jan.haml;

public interface Token<T> {
    int tryEat(String line, int position, T parsingResult) throws ParseException;
}
