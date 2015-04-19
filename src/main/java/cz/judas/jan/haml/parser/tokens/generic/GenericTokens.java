package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.AnyNumberOfCharToken;
import cz.judas.jan.haml.parser.tokens.terminal.AtLeastOneCharToken;
import cz.judas.jan.haml.parser.tokens.terminal.AtMostOneCharToken;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> Token<T> anyNumberOf(Token<? super T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static Token<Object> anyNumberOf(char matchingChar) {
        return anyNumberOf(c -> c == matchingChar);
    }

    public static Token<Object> anyNumberOf(CharPredicate predicate) {
        return new AnyNumberOfCharToken<>(predicate);
    }

    @SafeVarargs
    public static <T> Token<T> anyOf(Token<? super T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    public static <T> Token<T> sequence(Token<? super T> firstToken, Token<? super T> secondToken) {
        return new TwoItemSequenceToken<>(firstToken, secondToken);
    }

    public static <T> Token<T> sequence(Token<? super T> firstToken, Token<? super T> secondToken, Token<? super T> thirdToken) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken);
    }

    public static <T> Token<T> relaxedSequence(Token<? super T> firstToken, Token<? super T> secondToken) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken));
    }

    public static <T> Token<T> relaxedSequence(Token<? super T> firstToken, Token<? super T> secondToken, Token<? super T> thirdToken) {
        return new ThreeItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), precededWithWhitespace(thirdToken));
    }

    private static <T> PrecededWithWhitespaceToken<T> precededWithWhitespace(Token<? super T> secondToken) {
        return new PrecededWithWhitespaceToken<>(secondToken);
    }

    public static <T> Token<T> atMostOne(Token<? super T> token) {
        return new AtMostOneToken<>(token);
    }

    public static <T> Token<T> atMostOne(char matchingChar) {
        return new AtMostOneCharToken<>(c -> c ==matchingChar);
    }

    public static <T> Token<T> atLeastOne(Token<? super T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static Token<Object> atLeastOneChar(CharPredicate predicate) {
        return new AtLeastOneCharToken<>(predicate);
    }

    public static <I, O> Token<I> contextSwitch(Supplier<? extends O> contextSupplier, Token<? super O> inner, BiConsumer<? super I, ? super O> onSuccess) {
        return new ContextSwitchToken<>(contextSupplier, inner, onSuccess);
    }

    public static <T> Token<T> line(Token<T> lineContent) {
        return sequence(
                lineContent,
                atMostOne('\n')
        );
    }

    public static <T> Token<T> delimited(char startDelimiter, Token<? super T> token, char endDelimiter) {
        return new DelimitedToken<>(startDelimiter, token, endDelimiter);
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