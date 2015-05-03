package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;
import java.util.Optional;

public class AnyNumberOfToken<C, T> implements TypedToken<C, List<T>> {
    private final TypedToken<? super C, ? extends T> inner;

    public AnyNumberOfToken(TypedToken<? super C, ? extends T> inner) {
        this.inner = inner;
    }

    @Override
    public Optional<List<T>> tryEat(InputString line) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        while(line.hasMoreChars()) {
            Optional<? extends T> result = line.tryParse2(inner::tryEat);
            if(result.isPresent()) {
                builder.add(result.get());
            } else {
                break;
            }
        }
        return Optional.of((List<T>) builder.build());
    }
}
