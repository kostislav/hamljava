package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.TypedToken;
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
    public static <C, T> TypedToken<Object, List<T>> anyNumberOf(TypedToken<? super C, ? extends T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static TypedToken<Object, String> anyNumberOfChars(char matchingChar) {
        return anyNumberOfChars(c -> c == matchingChar);
    }

    public static TypedToken<Object, String> anyNumberOfChars(CharPredicate predicate) {
        return new AnyNumberOfCharToken(predicate);
    }

    @SafeVarargs
    public static <C, T> TypedToken<Object, T> anyOf(TypedToken<? super C, ? extends T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    public static <C, T1, T2, T> TypedToken<Object, T> sequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, secondToken, transform);
    }

    public static <C, T1, T2, T3, T> TypedToken<Object, T> sequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, TypedToken<? super C, ? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken, transform);
    }

    public static <C, T1, T2, T> TypedToken<Object, T> relaxedSequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), transform);
    }

    public static <C, T1, T2, T3, T> TypedToken<Object, T> relaxedSequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, TypedToken<? super C, ? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), precededWithWhitespace(thirdToken), transform);
    }

    private static <C, T> TypedToken<Object, T> precededWithWhitespace(TypedToken<? super C, ? extends T> secondToken) {
        return new PrecededWithWhitespaceToken<>(secondToken);
    }

    public static <C, T> TypedToken<Object, Optional<T>> atMostOne(TypedToken<? super C, ? extends T> token) {
        return new AtMostOneToken<>(token);
    }

    public static TypedToken<Object, String> atMostOne(char matchingChar) {
        return new AtMostOneCharToken(c -> c ==matchingChar);
    }

    public static <C, T> TypedToken<Object, List<T>> atLeastOne(TypedToken<? super C, ? extends T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static TypedToken<Object, String> atLeastOneChar(CharPredicate predicate) {
        return new AtLeastOneCharToken(predicate);
    }

    public static <C, T> TypedToken<Object, T> line(TypedToken<? super C, ? extends T> lineContent) {
        return sequence(
                lineContent,
                endOfLine(),
                (content, newLine) -> content
        );
    }

    public static <C, T> TypedToken<Object, T> delimited(char startDelimiter, TypedToken<? super C, ? extends T> token, char endDelimiter) {
        return new DelimitedToken<>(startDelimiter, token, endDelimiter);
    }

    public static <C, IT, OT> TypedToken<Object, OT> transformation(TypedToken<? super C, ? extends IT> token, Function<IT, OT> transform) {
        return new TransformationToken<>(token, transform);
    }
}