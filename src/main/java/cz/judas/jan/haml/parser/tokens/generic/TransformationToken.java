package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;
import java.util.function.Function;

public class TransformationToken<C, IT, OT> implements TypedToken<C, OT> {
    private final TypedToken<? super C, ? extends IT> token;
    private final Function<IT, OT> transform;

    public TransformationToken(TypedToken<? super C, ? extends IT> token, Function<IT, OT> transform) {
        this.token = token;
        this.transform = transform;
    }

    @Override
    public Optional<OT> tryEat(InputString line) {
        return token.tryEat(line).map(transform);
    }
}
