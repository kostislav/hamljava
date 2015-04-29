package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;
import java.util.Optional;

public class AtLeastOneToken<C, T> implements TypedToken<C, List<T>> {
    private final TypedToken<? super C, ? extends T> token;
    private final AnyNumberOfToken<? super C, ? extends T> restMatcher;

    public AtLeastOneToken(TypedToken<? super C, ? extends T> token) {
        this.token = token;
        restMatcher = new AnyNumberOfToken<>(token);
    }

    @Override
    public Optional<List<T>> tryEat(InputString line, C parsingResult) {
        Optional<? extends T> result = line.tryParse2(inputString -> token.tryEat(line, parsingResult));
        if(result.isPresent()) {
            List<? extends T> rest = restMatcher.tryEat(line, parsingResult).get();
            List<T> results = ImmutableList.<T>builder().add(result.get()).addAll(rest).build();
            return Optional.of(results);
        } else {
            return Optional.empty();
        }
    }
}
