package cz.judas.jan.haml.predicates;

import cz.judas.jan.haml.parser.CharPredicate;

@SuppressWarnings("UtilityClass")
public class Predicates {
    public static final CharPredicate ID_OR_CLASS_CHAR = c -> Character.isLetterOrDigit(c) || c == '-' || c == '_';

    public static final CharPredicate TAG_NAME_CHAR = Character::isLetterOrDigit;

    public static CharPredicate not(CharPredicate original) {
        return c -> !original.test(c);
    }

    public static CharPredicate anyOfChars(char... chars) {
        return c -> {
            for (char aChar : chars) {
                if(aChar == c) {
                    return true;
                }
            }
            return false;
        };
    }
}
