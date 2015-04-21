package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;

import java.util.List;

public class AnyOfToken<C, T> implements TypedToken<C, T> {
    private final List<Token<? super C>> alternatives;

    public AnyOfToken(Iterable<? extends Token<? super C>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        for (Token<? super C> alternative : alternatives) {
            if(line.tryParse(inputString -> alternative.tryEat(inputString, parsingResult))) {
                return true;
            }
        }
        return false;
    }
}
