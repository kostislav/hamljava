package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;
import java.util.Optional;

public class AnyNumberOfToken<T> implements Token<List<T>> {
    private final Token<? extends T> inner;

    public AnyNumberOfToken(Token<? extends T> inner) {
        this.inner = inner;
    }

    @Override
    public Optional<? extends List<T>> tryEat(InputString line) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        while(line.hasMoreChars()) {
            Optional<? extends T> result = line.tryParse2(inner::tryEat);
            if(result.isPresent()) {
                builder.add(result.get());
            } else {
                break;
            }
        }
        return Optional.of(builder.build());
    }
}
