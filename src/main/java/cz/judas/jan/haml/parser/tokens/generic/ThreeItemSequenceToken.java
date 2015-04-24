package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class ThreeItemSequenceToken<C, T1, T2, T3, T> implements TypedToken<C, T> {
    private final TypedToken<? super C, ? extends T1> firstToken;
    private final TypedToken<? super C, ? extends T2> secondToken;
    private final TypedToken<? super C, ? extends T3> thirdToken;
    private final TriFunction<T1, T2, T3, T> transform;

    public ThreeItemSequenceToken(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, TypedToken<? super C, ? extends T3> thirdToken, TriFunction<T1, T2, T3, T> transform) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.thirdToken = thirdToken;
        this.transform = transform;
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
