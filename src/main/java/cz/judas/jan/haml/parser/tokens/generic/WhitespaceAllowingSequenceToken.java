package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;
import cz.judas.jan.haml.util.InterleavedIterable;

public class WhitespaceAllowingSequenceToken<T> implements Token<T> {
    private final Token<? super T> tokens;

    public WhitespaceAllowingSequenceToken(Iterable<? extends Token<? super T>> tokens) {
        this.tokens = new SequenceOfTokens<>(new InterleavedIterable<>(tokens, Terminals.<T>whitespace()));
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) {
        return tokens.tryEat(line, position, parsingResult);
    }
}
