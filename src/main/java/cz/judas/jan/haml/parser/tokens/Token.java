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

    default boolean tryEat(InputString line, T parsingResult) {
        int newPosition = tryEat(line.wholeLine(), line.currentPosition(), parsingResult);
        if(newPosition != -1) {
            line.setCurrentPosition(newPosition);
            return true;
        } else {
            return false;
        }
    }
}
