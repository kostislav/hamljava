package cz.judas.jan.haml.parser.tokens.generic;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.List;

public class AnyOfToken<T> implements Token<T> {
    private final List<Token<? super T>> alternatives;

    public AnyOfToken(Iterable<? extends Token<? super T>> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public int tryEat(InputString line, T parsingResult) {
        for (Token<? super T> alternative : alternatives) {
            if(line.tryParse(inputString -> alternative.tryEat(inputString, parsingResult) != -1)) {
                return line.currentPosition();
            }
        }
        return -1;
    }
}
