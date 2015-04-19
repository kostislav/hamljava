package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.*;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.endOfLine;

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

    public static <C, T1, T2, T> Token<C> sequence(Token<? super C> firstToken, Token<? super C> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, secondToken);
    }

    public static <C, T1, T2, T3, T> Token<C> sequence(Token<? super C> firstToken, Token<? super C> secondToken, Token<? super C> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken);
    }

    public static <C, T1, T2, T> Token<C> relaxedSequence(Token<? super C> firstToken, Token<? super C> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken));
    }

    public static <C, T1, T2, T3, T> Token<C> relaxedSequence(Token<? super C> firstToken, Token<? super C> secondToken, Token<? super C> thirdToken, TriFunction<T1, T2, T3, T> transform) {
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

    public static <C, T> Token<C> line(Token<C> lineContent) {
        return GenericTokens.<C, T, Character, T>sequence(
                lineContent,
                endOfLine(),
                (content, newLine) -> content
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