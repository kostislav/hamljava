package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

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
}
