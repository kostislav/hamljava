package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.function.BiFunction;

public class TwoItemSequenceToken<C, T1, T2, T> implements TypedToken<C, T> {
    private final TypedToken<? super C, ? extends T1> firstToken;
    private final TypedToken<? super C, ? extends T2> secondToken;
    private final BiFunction<T1, T2, T> transform;

    public TwoItemSequenceToken(TypedToken<? super C, ? extends T1> firstToken, TypedToken<? super C, ? extends T2> secondToken, BiFunction<T1, T2, T> transform) {
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.transform = transform;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(
                inputString -> firstToken.tryEat(inputString, parsingResult)
                        && secondToken.tryEat(inputString, parsingResult)
        );
    }
}
