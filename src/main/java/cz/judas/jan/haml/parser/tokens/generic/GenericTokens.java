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
    public static <C, T> TypedToken<C, List<T>> anyNumberOf(Token<? super C> inner) {
        return new AnyNumberOfToken<>(inner);
    }

    public static TypedToken<Object, String> anyNumberOf(char matchingChar) {
        return anyNumberOf(c -> c == matchingChar);
    }

    public static TypedToken<Object, String> anyNumberOf(CharPredicate predicate) {
        return new AnyNumberOfCharToken(predicate);
    }

    @SafeVarargs
    public static <C, T> TypedToken<C, T> anyOf(Token<? super C>... alternatives) {
        return new AnyOfToken<>(ImmutableList.copyOf(alternatives));
    }

    public static <C, T1, T2, T> TypedToken<C, T> sequence(Token<? super C> firstToken, Token<? super C> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, secondToken);
    }

    public static <C, T1, T2, T3, T> TypedToken<C, T> sequence(Token<? super C> firstToken, Token<? super C> secondToken, Token<? super C> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, secondToken, thirdToken);
    }

    public static <C, T1, T2, T> TypedToken<C, T> relaxedSequence(Token<? super C> firstToken, Token<? super C> secondToken, BiFunction<T1, T2, T> transform) {
        return new TwoItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken));
    }

    public static <C, T1, T2, T3, T> TypedToken<C, T> relaxedSequence(Token<? super C> firstToken, Token<? super C> secondToken, Token<? super C> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        return new ThreeItemSequenceToken<>(firstToken, precededWithWhitespace(secondToken), precededWithWhitespace(thirdToken));
    }

    private static <C, T> TypedToken<C, T> precededWithWhitespace(Token<? super C> secondToken) {
        return new PrecededWithWhitespaceToken<>(secondToken);
    }

    public static <C, T> TypedToken<C, Optional<T>> atMostOne(Token<? super C> token) {
        return new AtMostOneToken<>(token);
    }

    public static TypedToken<Object, Optional<Character>> atMostOne(char matchingChar) {
        return new AtMostOneCharToken(c -> c ==matchingChar);
    }

    public static <C, T> TypedToken<C, List<T>> atLeastOne(Token<? super C> token) {
        return new AtLeastOneToken<>(token);
    }

    public static TypedToken<Object, String> atLeastOneChar(CharPredicate predicate) {
        return new AtLeastOneCharToken<>(predicate);
    }

    public static <IC, OC, T> TypedToken<IC, T> contextSwitch(Supplier<? extends OC> contextSupplier, Token<? super OC> inner, BiConsumer<? super IC, ? super OC> onSuccess) {
        return new ContextSwitchToken<>(contextSupplier, inner, onSuccess);
    }

    public static <C, T> TypedToken<C, T> line(Token<? super C> lineContent) {
        return GenericTokens.<C, T, Character, T>sequence(
                lineContent,
                endOfLine(),
                (content, newLine) -> content
        );
    }

    public static <C, T> TypedToken<C, T> delimited(char startDelimiter, Token<? super C> token, char endDelimiter) {
        return new DelimitedToken<>(startDelimiter, token, endDelimiter);
    }

    public static <C, T> TypedToken<C, T> onMatch(Token<? super C> token, BiConsumer<? super C, String> onMatch) {
        return new OnMatchToken<>(token, onMatch);
    }

    public static <C, IT, OT> TypedToken<C, OT> transformation(Token<C> token, Function<IT, OT> transform) {
        return new TransformationToken<>(token, transform);
    }

    @SuppressWarnings("UnusedParameters") // class argument is for type inference only
    public static <C, T> MatchHelper<C, T> match(Token<? super C> token, Class<? extends C> clazz) {
        return new MatchHelper<>(token);
    }

    public static class MatchHelper<C, T> {
        private final Token<? super C> token;

        public MatchHelper(Token<? super C> token) {
            this.token = token;
        }

        public TypedToken<C, T> to(BiConsumer<? super C, String> onMatch) {
            return new OnMatchToken<>(token, onMatch);
        }
    }
}