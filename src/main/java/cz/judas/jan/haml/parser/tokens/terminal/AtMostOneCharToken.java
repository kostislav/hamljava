package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;

public class AtMostOneCharToken implements TypedToken<Object, Optional<Character>> {
    private final CharPredicate predicate;

    public AtMostOneCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, Object parsingResult) {
        return line.tryGetSubstringIf(predicate, i -> i <= 1).isPresent();
    }
}
