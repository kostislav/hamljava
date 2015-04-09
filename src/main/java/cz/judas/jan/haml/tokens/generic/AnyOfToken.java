package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

import java.util.List;

public class AnyOfToken<T> implements Token<T> {
    private final List<Token<T>> alternatives;

    public AnyOfToken(Iterable<? extends Token<T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        for (Token<T> alternative : alternatives) {
            int newPosition = alternative.tryEat(line, position, parsingResult);
            if(newPosition != -1) {
                return newPosition;
            }
        }

        return -1;
    }

    public static <T> AnyOfToken<T> anyOf(Iterable<? extends Token<T>> alternatives) {
        return new AnyOfToken<>(alternatives);
    }
}
