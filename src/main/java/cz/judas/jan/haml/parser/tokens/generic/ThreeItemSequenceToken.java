package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class ThreeItemSequenceToken<C, T1, T2, T3, T> implements TypedToken<C, T> {
    private final Token<? super C> firstToken;
    private final Token<? super C> secondToken;
    private final Token<? super C> thirdToken;

    public ThreeItemSequenceToken(Token<? super C> firstToken, Token<? super C> secondToken, Token<? super C> thirdToken) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.thirdToken = thirdToken;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(
                inputString -> firstToken.tryEat(inputString, parsingResult)
                        && secondToken.tryEat(inputString, parsingResult)
                        && thirdToken.tryEat(inputString, parsingResult)
        );
    }
}
