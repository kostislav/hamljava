package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class TwoItemSequenceToken<C, T1, T2, T> implements TypedToken<C, T> {
    private final Token<? super C> firstToken;
    private final Token<? super C> secondToken;

    public TwoItemSequenceToken(Token<? super C> firstToken, Token<? super C> secondToken) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(
                inputString -> firstToken.tryEat(inputString, parsingResult)
                        && secondToken.tryEat(inputString, parsingResult)
        );
    }
}
