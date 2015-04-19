package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class ThreeItemSequenceToken<T> implements Token<T> {
    private final Token<? super T> firstToken;
    private final Token<? super T> secondToken;
    private final Token<? super T> thirdToken;

    public ThreeItemSequenceToken(Token<? super T> firstToken, Token<? super T> secondToken, Token<? super T> thirdToken) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.thirdToken = thirdToken;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(
                inputString -> firstToken.tryEat(inputString, parsingResult)
                        && secondToken.tryEat(inputString, parsingResult)
                        && thirdToken.tryEat(inputString, parsingResult)
        );
    }
}
