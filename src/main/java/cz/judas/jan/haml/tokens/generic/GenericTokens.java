package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.tokens.Token;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> AnyNumberOfToken<T> anyNumberOf(Token<T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static <T> AnyOfToken<T> anyOf(Iterable<? extends Token<T>> alternatives) {
        return new AnyOfToken<>(alternatives);
    }

    public static <T> SequenceOfTokens<T> sequence(Iterable<? extends Token<T>> tokens) {
        return new SequenceOfTokens<>(tokens);
    }

    public static <T> AtMostOneToken<T> atMostOne(Token<T> token) {
        return new AtMostOneToken<>(token);
    }
}