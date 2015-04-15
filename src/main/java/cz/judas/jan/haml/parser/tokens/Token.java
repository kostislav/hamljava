package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

public interface Token<T> {
    default int tryEat(String line, int position, T parsingResult) {
        return tryEat(new InputString(line, position), parsingResult);
    }

    default int tryEat(InputString line, T parsingResult) {
        int newPosition = tryEat(line.wholeLine(), line.currentPosition(), parsingResult);
        if(newPosition != -1) {
            line.setCurrentPosition(newPosition);
        }
        return newPosition;
    }
}
