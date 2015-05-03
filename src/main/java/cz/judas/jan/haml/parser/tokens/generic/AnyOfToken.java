package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;
import java.util.Optional;

public class AnyOfToken<T> implements Token<T> {
    private final List<Token<? extends T>> alternatives;

    public AnyOfToken(Iterable<? extends Token<? extends T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public Optional<? extends T> tryEat(InputString line) {
        for (Token<? extends T> alternative : alternatives) {
            Optional<? extends T> result = line.tryParse2(alternative::tryEat);
            if(result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }
}
