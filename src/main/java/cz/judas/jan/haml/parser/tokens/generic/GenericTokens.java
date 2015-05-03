package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.AnyNumberOfCharToken;
import cz.judas.jan.haml.parser.tokens.terminal.AtLeastOneCharToken;
import cz.judas.jan.haml.parser.tokens.terminal.AtMostOneCharToken;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.endOfLine;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <T> Token<List<T>> anyNumberOf(Token<? extends T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static Token<String> anyNumberOfChars(char matchingChar) {
        return anyNumberOfChars(c -> c == matchingChar);
    }

    public static Token<String> anyNumberOfChars(CharPredicate predicate) {
        return new AnyNumberOfCharToken(predicate);
    }

    @SafeVarargs
    public static <T> Token<T> anyOf(Token<? extends T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    public static <T1, T2, T> Token<T> sequence(Token<? extends T1> firstToken, Token<? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, secondToken, transform);
    }

    public static <T1, T2, T3, T> Token<T> sequence(Token<? extends T1> firstToken, Token<? extends T2> secondToken, Token<? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken, transform);
    }

    public static <T1, T2, T> Token<T> relaxedSequence(Token<? extends T1> firstToken, Token<? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), transform);
    }

    public static <T1, T2, T3, T> Token<T> relaxedSequence(Token<? extends T1> firstToken, Token<? extends T2> secondToken, Token<? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), precededWithWhitespace(thirdToken), transform);
    }

    private static <T> Token<T> precededWithWhitespace(Token<? extends T> secondToken) {
        return new PrecededWithWhitespaceToken<>(secondToken);
    }

    public static <T> Token<Optional<T>> atMostOne(Token<? extends T> token) {
        return new AtMostOneToken<>(token);
    }

    public static Token<String> atMostOne(char matchingChar) {
        return new AtMostOneCharToken(c -> c ==matchingChar);
    }

    public static <T> Token<List<T>> atLeastOne(Token<? extends T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static Token<String> atLeastOneChar(CharPredicate predicate) {
        return new AtLeastOneCharToken(predicate);
    }

    public static <T> Token<T> line(Token<? extends T> lineContent) {
        return sequence(
                lineContent,
                endOfLine(),
                (content, newLine) -> content
        );
    }

    public static <T> Token<T> delimited(char startDelimiter, Token<? extends T> token, char endDelimiter) {
        return new DelimitedToken<>(startDelimiter, token, endDelimiter);
    }

    public static <IT, OT> Token<OT> transformation(Token<? extends IT> token, Function<IT, OT> transform) {
        return new TransformationToken<>(token, transform);
    }
}