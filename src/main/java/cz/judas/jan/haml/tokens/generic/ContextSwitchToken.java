package cz.judas.jan.haml.tokens.generic;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.Token;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ContextSwitchToken<I, O> implements Token<I> {
    private final Supplier<O> contextSupplier;
    private final Token<O> inner;
    private final BiConsumer<I, O> onSuccess;

    public ContextSwitchToken(Supplier<O> contextSupplier, Token<O> inner, BiConsumer<I, O> onSuccess) {
        this.contextSupplier = contextSupplier;
        this.inner = inner;
        this.onSuccess = onSuccess;
    }

    @Override
    public int tryEat(String line, int position, I parsingResult) throws ParseException {
        O newContext = contextSupplier.get();
        int newPosition = inner.tryEat(line, position, newContext);
        if (newPosition != -1) {
            onSuccess.accept(parsingResult, newContext);
        }
        return newPosition;
    }
}
