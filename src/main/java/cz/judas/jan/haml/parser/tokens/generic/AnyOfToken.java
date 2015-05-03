package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;
import java.util.Optional;

public class AnyOfToken<C, T> implements TypedToken<Object, T> {
    private final List<TypedToken<?, ? extends T>> alternatives;

    public AnyOfToken(Iterable<? extends TypedToken<?, ? extends T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public Optional<T> tryEat(InputString line) {
        for (TypedToken<?, ? extends T> alternative : alternatives) {
            Optional<? extends T> result = line.tryParse2(alternative::tryEat);
            if(result.isPresent()) {
                return (Optional<T>)result;
            }
        }
        return Optional.empty();
    }
}
