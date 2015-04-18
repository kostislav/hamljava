package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ContextSwitchToken<I, O> implements Token<I> {
    private final Supplier<? extends O> contextSupplier;
    private final Token<? super O> inner;
    private final BiConsumer<? super I, ? super O> onSuccess;

    public ContextSwitchToken(Supplier<? extends O> contextSupplier, Token<? super O> inner, BiConsumer<? super I, ? super O> onSuccess) {
        this.contextSupplier = contextSupplier;
        this.inner = inner;
        this.onSuccess = onSuccess;
    }

    @Override
    public boolean tryEat(InputString line, I parsingResult) {
        O newContext = contextSupplier.get();
        if(inner.tryEat(line, newContext)) {
            onSuccess.accept(parsingResult, newContext);
            return true;
        } else {
            return false;
        }
    }
}
