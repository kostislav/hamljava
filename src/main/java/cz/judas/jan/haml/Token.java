package cz.judas.jan.haml;

public interface Token {
    int tryEat(String line, int position, ParsingResult parsingResult) throws ParseException;
}
