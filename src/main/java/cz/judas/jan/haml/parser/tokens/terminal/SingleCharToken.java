package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class SingleCharToken implements Token<Object> {
    private final CharPredicate predicate;

    public SingleCharToken(char c) {
        predicate = input -> input == c;
    }

    public SingleCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public int tryEat(InputString line, Object parsingResult) {
        if (line.currentCharIs(predicate)) {
            line.advance();
            return line.currentPosition();
        } else {
            return -1;
        }
    }
}
