package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.SingleCharToken;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> AnyNumberOfToken<T> anyNumberOf(Token<? super T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static AnyNumberOfToken<Object> anyNumberOf(char c) {
        return new AnyNumberOfToken<>(new SingleCharToken(c));
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

    public static <T> AtMostOneToken<T> atMostOne(char c) {
        return new AtMostOneToken<>(new SingleCharToken(c));
    }

    public static <T> AtLeastOneToken<T> atLeastOne(Token<? super T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static AtLeastOneToken<Object> atLeastOne(CharPredicate predicate) {
        return new AtLeastOneToken<>(new SingleCharToken(predicate));
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

    @SuppressWarnings("UnusedParameters") // class argument is for type inference only
    public static <T> MatchHelper<T> match(Token<? super T> token, Class<? extends T> clazz) {
        return new MatchHelper<>(token);
    }

    public static class MatchHelper<T> {
        private final Token<? super T> token;

        public MatchHelper(Token<? super T> token) {
            this.token = token;
        }

        public Token<T> to(BiConsumer<? super T, String> onMatch) {
            return new OnMatchToken<>(token, onMatch);
        }
    }
}