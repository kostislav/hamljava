package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;

public class SequenceOfTokens<T> implements Token<T> {
    private final List<Token<? super T>> sequence;

    public SequenceOfTokens(Iterable<? extends Token<? super T>> sequence) {
        this.sequence = ImmutableList.copyOf(sequence);
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(inputString -> sequence.stream().allMatch(token -> token.tryEat(inputString, parsingResult)));
    }
}
