package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tokens.Token;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> AnyNumberOfToken<T> anyNumberOf(Token<T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    @SafeVarargs
    public static <T> AnyOfToken<T> anyOf(Token<T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    @SafeVarargs
    public static <T> SequenceOfTokens<T> sequence(Token<T>... tokens) {
        return new SequenceOfTokens<>(ImmutableList.copyOf(tokens));
    }

    public static <T> AtMostOneToken<T> atMostOne(Token<T> token) {
        return new AtMostOneToken<>(token);
    }
}