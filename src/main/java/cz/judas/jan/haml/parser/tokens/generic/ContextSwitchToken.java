package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ContextSwitchToken<IC, OC, T> implements TypedToken<IC, T> {
    private final Supplier<? extends OC> contextSupplier;
    private final Token<? super OC> inner;
    private final BiConsumer<? super IC, ? super OC> onSuccess;

    public ContextSwitchToken(Supplier<? extends OC> contextSupplier, Token<? super OC> inner, BiConsumer<? super IC, ? super OC> onSuccess) {
        this.contextSupplier = contextSupplier;
        this.inner = inner;
        this.onSuccess = onSuccess;
    }

    @Override
    public boolean tryEat(InputString line, IC parsingResult) {
        OC newContext = contextSupplier.get();
        if(inner.tryEat(line, newContext)) {
            onSuccess.accept(parsingResult, newContext);
            return true;
        } else {
            return false;
        }
    }
}
