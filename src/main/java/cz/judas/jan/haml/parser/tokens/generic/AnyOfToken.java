package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;

public class AnyOfToken<T> implements Token<T> {
    private final List<Token<? super T>> alternatives;

    public AnyOfToken(Iterable<? extends Token<? super T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        for (Token<? super T> alternative : alternatives) {
            int newPosition = alternative.tryEat(line, position, parsingResult);
            if(newPosition != -1) {
                return newPosition;
            }
        }

        return -1;
    }
}
