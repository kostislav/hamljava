package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class EndOfLineToken implements TypedToken<Object, Optional<Character>> {
    @Override
    public boolean tryEat(InputString line, Object parsingResult) {
        if(line.currentCharIs('\n') || !line.hasMoreChars()) {
            line.advance();
            return true;
        } else {
            return false;
        }
    }
}
