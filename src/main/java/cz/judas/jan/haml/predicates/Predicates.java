package cz.judas.jan.haml.predicates;

import cz.judas.jan.haml.parser.CharPredicate;

@SuppressWarnings("UtilityClass")
public class Predicates {
    public static final CharPredicate ID_OR_CLASS_CHAR = c -> Character.isLetterOrDigit(c) || c == '-' || c == '_';

    public static final CharPredicate TAG_NAME_CHAR = Character::isLetterOrDigit;
}
