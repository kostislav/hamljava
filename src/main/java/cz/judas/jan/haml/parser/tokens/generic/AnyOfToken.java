package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;
import java.util.Optional;

public class AnyOfToken<C, T> implements TypedToken<C, T> {
    private final List<TypedToken<? super C, ? extends T>> alternatives;

    public AnyOfToken(Iterable<? extends TypedToken<? super C, ? extends T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public Optional<T> tryEat2(InputString line, C parsingResult) {
        for (TypedToken<? super C, ? extends T> alternative : alternatives) {
            Optional<? extends T> result = line.tryParse2(inputString -> alternative.tryEat2(inputString, parsingResult));
            if(result.isPresent()) {
                return (Optional<T>)result;
            }
        }
        return Optional.empty();
    }
}
