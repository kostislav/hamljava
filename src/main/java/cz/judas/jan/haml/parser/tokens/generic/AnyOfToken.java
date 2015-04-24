package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;

public class AnyOfToken<C, T> implements TypedToken<C, T> {
    private final List<TypedToken<? super C, ? extends T>> alternatives;

    public AnyOfToken(Iterable<? extends TypedToken<? super C, ? extends T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        for (TypedToken<? super C, ? extends T> alternative : alternatives) {
            if(line.tryParse(inputString -> alternative.tryEat(inputString, parsingResult))) {
                return true;
            }
        }
        return false;
    }
}
