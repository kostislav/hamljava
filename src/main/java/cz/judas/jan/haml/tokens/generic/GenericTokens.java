package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> AnyNumberOfToken<T> anyNumberOf(Token<? super T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    @SafeVarargs
    public static <T> AnyOfToken<T> anyOf(Token<? super T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    @SafeVarargs
    public static <T> SequenceOfTokens<T> sequence(Token<? super T>... tokens) {
        return new SequenceOfTokens<>(ImmutableList.copyOf(tokens));
    }

    public static <T> AtMostOneToken<T> atMostOne(Token<? super T> token) {
        return new AtMostOneToken<>(token);
    }

    public static <T> AtLeastOneToken<T> atLeastOne(Token<? super T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static <I, O> ContextSwitchToken<I, O> contextSwitch(Supplier<? extends O> contextSupplier, Token<? super O> inner, BiConsumer<? super I, ? super O> onSuccess) {
        return new ContextSwitchToken<>(contextSupplier, inner, onSuccess);
    }

    @SafeVarargs
    public static <T> WhitespaceAllowingSequenceToken<T> relaxedSequence(Token<? super T>... tokens) {
        return new WhitespaceAllowingSequenceToken<>(ImmutableList.copyOf(tokens));
    }

    public static <T> Token<T> onMatch(Token<? super T> token, BiConsumer<? super T, String> onMatch) {
        return new OnMatchToken<>(token, onMatch);
    }
}