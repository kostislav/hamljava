package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.function.Function;

public class TransformationToken<C, IT, OT> implements TypedToken<C, OT> {
    private final Token<C> token;
    private final Function<IT, OT> transform;

    public TransformationToken(Token<C> token, Function<IT, OT> transform) {
        this.token = token;
        this.transform = transform;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return token.tryEat(line, parsingResult);
    }
}
