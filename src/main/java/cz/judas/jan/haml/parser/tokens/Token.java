package cz.judas.jan.haml.parser.tokens;

public interface Token<T> {
    int tryEat(String line, int position, T parsingResult);
}
