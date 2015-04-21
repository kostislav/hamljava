package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class AnyNumberOfCharToken implements TypedToken<Object, String> {
    private final CharPredicate predicate;

    public AnyNumberOfCharToken(CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean tryEat(InputString line, Object parsingResult) {
        return line.tryGetSubstringIf(predicate, i -> i >= 0).isPresent();
    }
}
