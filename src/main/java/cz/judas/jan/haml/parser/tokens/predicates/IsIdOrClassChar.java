package cz.judas.jan.haml.parser.tokens.predicates;

import cz.judas.jan.haml.parser.CharPredicate;

public class IsIdOrClassChar implements CharPredicate {
    @Override
    public boolean test(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
    }
}
