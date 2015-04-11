package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

import java.util.List;

public class SequenceOfTokens<T> implements Token<T> {
    private final List<Token<? super T>> sequence;

    public SequenceOfTokens(Iterable<? extends Token<? super T>> sequence) {
        this.sequence = ImmutableList.copyOf(sequence);
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int currentPosition = position;
        for (Token<? super T> token : sequence) {
            currentPosition = token.tryEat(line, currentPosition, parsingResult);
            if(currentPosition == -1) {
                return -1;
            }
        }
        return currentPosition;
    }
}
