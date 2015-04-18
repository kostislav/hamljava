package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class TwoItemSequenceToken<T> implements Token<T> {
    private final Token<? super T> firstToken;
    private final Token<? super T> secondToken;

    public TwoItemSequenceToken(Token<? super T> firstToken, Token<? super T> secondToken) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(inputString -> firstToken.tryEat(inputString, parsingResult) && secondToken.tryEat(inputString, parsingResult));
    }
}
