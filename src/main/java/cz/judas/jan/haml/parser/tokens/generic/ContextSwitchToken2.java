package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.Optional;
import java.util.function.BiConsumer;

public class ContextSwitchToken2<IC, T> implements TypedToken<IC, T> {
    private final TypedToken<Object, ? extends T> inner;
    private final BiConsumer<? super IC, ? super T> onSuccess;

    public ContextSwitchToken2(TypedToken<Object, ? extends T> inner, BiConsumer<? super IC, ? super T> onSuccess) {
        this.inner = inner;
        this.onSuccess = onSuccess;
    }

    @Override
    public Optional<T> tryEat(InputString line, IC parsingResult) {
        Optional<? extends T> result = inner.tryEat(line, new Object());
        if(result.isPresent()) {
            onSuccess.accept(parsingResult, result.get());
        }

        return (Optional<T>)result;
    }
}
