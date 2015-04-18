package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

public interface Token<T> {
    default int tryEat(String line, int position, T parsingResult) {
        InputString inputString = new InputString(line, position);
        if(tryEat(inputString, parsingResult)) {
            return inputString.currentPosition();
        } else {
            return -1;
        }
    }

    boolean tryEat(InputString line, T parsingResult);
}
