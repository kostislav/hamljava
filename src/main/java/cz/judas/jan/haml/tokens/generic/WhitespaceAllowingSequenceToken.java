package cz.judas.jan.haml.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

import java.util.Iterator;

public class WhitespaceAllowingSequenceToken<T> implements Token<T> {
    private final Token<T> tokens;

    public WhitespaceAllowingSequenceToken(Iterable<? extends Token<T>> tokens) {
        ImmutableList.Builder<Token<T>> builder = ImmutableList.builder();
        Iterator<? extends Token<T>> iterator = tokens.iterator();
        if(iterator.hasNext()) {
            builder.add(iterator.next());
        }
        while(iterator.hasNext()) {
            builder.add(Terminals.<T>whitespace()).add(iterator.next());
        }
        this.tokens = new SequenceOfTokens<>(builder.build());
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        return tokens.tryEat(line, position, parsingResult);
    }
}
