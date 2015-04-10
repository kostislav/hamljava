package cz.judas.jan.haml.tokens.predicates;

import cz.judas.jan.haml.CharPredicate;

public class IsTagNameChar implements CharPredicate {
    @Override
    public boolean test(char c) {
        return Character.isLetterOrDigit(c);
    }
}
