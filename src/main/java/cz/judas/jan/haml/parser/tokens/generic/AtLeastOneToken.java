package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;
import java.util.Optional;

public class AtLeastOneToken<T> implements Token<List<T>> {
    private final Token<? extends T> token;
    private final AnyNumberOfToken<? extends T> restMatcher;

    public AtLeastOneToken(Token<? extends T> token) {
        this.token = token;
        restMatcher = new AnyNumberOfToken<>(token);
    }

    @Override
    public Optional<List<T>> tryEat(InputString line) {
        return line.tryParse2(inputString -> {
            Optional<? extends T> result = token.tryEat(line);
            if (result.isPresent()) {
                List<? extends T> rest = restMatcher.tryEat(line).get();
                List<T> results = ImmutableList.<T>builder().add(result.get()).addAll(rest).build();
                return Optional.of(results);
            } else {
                return Optional.<List<T>>empty();
            }
        });
    }
}
