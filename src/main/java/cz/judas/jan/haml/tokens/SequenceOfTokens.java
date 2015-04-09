package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;

import java.util.List;

public class SequenceOfTokens<T> implements Token<T> {
    private final List<Token<T>> sequence;

    public SequenceOfTokens(Iterable<? extends Token<T>> sequence) {
        this.sequence = ImmutableList.copyOf(sequence);
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int currentPosition = position;
        for (Token<T> token : sequence) {
            currentPosition = token.tryEat(line, currentPosition, parsingResult);
            if(currentPosition == -1) {
                throw new ParseException(line, position);
            }
        }
        return currentPosition;
    }

    public static <T> SequenceOfTokens<T> sequence(Iterable<? extends Token<T>> tokens) {
        return new SequenceOfTokens<>(tokens);
    }
}
