package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.CharPredicate;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.terminal.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.endOfLine;

@SuppressWarnings("UtilityClass")
public class GenericTokens {
    public static <C, T> TypedToken<C, List<T>> anyNumberOf(TypedToken<? super C, ? extends T> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static TypedToken<Object, String> anyNumberOf(char matchingChar) {
        return anyNumberOf(c -> c == matchingChar);
    }

    public static TypedToken<Object, String> anyNumberOf(CharPredicate predicate) {
        return new AnyNumberOfCharToken(predicate);
    }

    @SafeVarargs
    public static <C, T> TypedToken<C, T> anyOf(TypedToken<? super C, ? extends T>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    public static <C, T1, T2, T> TypedToken<C, T> sequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, secondToken);
    }

    public static <C, T1, T2, T3, T> TypedToken<C, T> sequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, TypedToken<? super C, ? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken);
    }

    public static <C, T1, T2, T> TypedToken<C, T> relaxedSequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken));
    }

    public static <C, T1, T2, T3, T> TypedToken<C, T> relaxedSequence(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, TypedToken<? super C, ? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), precededWithWhitespace(thirdToken));
    }

    private static <C, T> TypedToken<C, T> precededWithWhitespace(TypedToken<? super C, ? extends T> secondToken) {
        return new PrecededWithWhitespaceToken<>(secondToken);
    }

    public static <C, T> TypedToken<C, Optional<T>> atMostOne(TypedToken<? super C, ? extends T> token) {
        return new AtMostOneToken<>(token);
    }

    public static TypedToken<Object, Optional<Character>> atMostOne(char matchingChar) {
        return new AtMostOneCharToken(c -> c ==matchingChar);
    }

    public static <C, T> TypedToken<C, List<T>> atLeastOne(TypedToken<? super C, ? extends T> token) {
        return new AtLeastOneToken<>(token);
    }

    public static TypedToken<Object, String> atLeastOneChar(CharPredicate predicate) {
        return new AtLeastOneCharToken<>(predicate);
    }

    public static <IC, OC, T> TypedToken<IC, T> contextSwitch(Supplier<? extends OC> contextSupplier, TypedToken<? super OC, ? extends T> inner, BiConsumer<? super IC, ? super OC> onSuccess) {
        return new ContextSwitchToken<>(contextSupplier, inner, onSuccess);
    }

    public static <C, T> TypedToken<C, T> line(TypedToken<? super C, ? extends T> lineContent) {
        return sequence(
                lineContent,
                endOfLine(),
                (content, newLine) -> content
        );
    }

    public static <C, T> TypedToken<C, T> delimited(char startDelimiter, TypedToken<? super C, ? extends T> token, char endDelimiter) {
        return new DelimitedToken<>(startDelimiter, token, endDelimiter);
    }

    public static <C, T> TypedToken<C, T> onMatch(TypedToken<? super C, ? extends T> token, BiConsumer<? super C, String> onMatch) {
        return new OnMatchToken<>(token, onMatch);
    }

    public static <C, IT, OT> TypedToken<C, OT> transformation(Token<C> token, Function<IT, OT> transform) {
        return new TransformationToken<>(token, transform);
    }

    @SuppressWarnings("UnusedParameters") // class argument is for type inference only
    public static <C, T> MatchHelper<C, T> match(TypedToken<? super C, ? extends T> token, Class<? extends C> clazz) {
        return new MatchHelper<>(token);
    }

    public static class MatchHelper<C, T> {
        private final TypedToken<? super C, ? extends T> token;

        public MatchHelper(TypedToken<? super C, ? extends T> token) {
            this.token = token;
        }

        public TypedToken<C, T> to(BiConsumer<? super C, String> onMatch) {
            return new OnMatchToken<>(token, onMatch);
        }
    }
}