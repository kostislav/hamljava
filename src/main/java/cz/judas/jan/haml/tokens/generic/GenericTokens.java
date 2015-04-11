package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

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

    public static <T> AtLeastOneToken<T> atLeastOne(Token<T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static <I, O> ContextSwitchToken<I, O> contextSwitch(Supplier<O> contextSupplier, Token<O> inner, BiConsumer<I, O> onSuccess) {
        return new ContextSwitchToken<>(contextSupplier, inner, onSuccess);
    }

    @SafeVarargs
    public static <T> WhitespaceAllowingSequenceToken<T> relaxedSequence(Token<T>... tokens) {
        return new WhitespaceAllowingSequenceToken<>(ImmutableList.copyOf(tokens));
    }
}