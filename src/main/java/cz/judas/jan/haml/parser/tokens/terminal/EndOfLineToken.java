package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class EndOfLineToken<T> implements Token<T> {
    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        if(line.currentCharIs('\n') || !line.hasMoreChars()) {
            line.advance();
            return true;
        } else {
            return false;
        }
    }
}
