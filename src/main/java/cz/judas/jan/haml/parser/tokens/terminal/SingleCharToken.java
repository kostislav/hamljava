package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class SingleCharToken implements TypedToken<Object, Character> {
    private final CharPredicate predicate;

    public SingleCharToken(char c) {
        predicate = input -> input == c;
    }

    public SingleCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, Object parsingResult) {
        if (line.currentCharIs(predicate)) {
            line.advance();
            return true;
        } else {
            return false;
        }
    }
}
