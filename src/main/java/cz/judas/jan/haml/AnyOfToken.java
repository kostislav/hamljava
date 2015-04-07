package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class AnyOfToken implements Token {
    private final List<Token> alternatives;

    public AnyOfToken(Iterable<Token> alternatives) {
        this.alternatives = ImmutableList.copyOf(alternatives);
    }

    @Override
    public int tryEat(String line, int position, ParsingResult parsingResult) throws ParseException {
        for (Token alternative : alternatives) {
            int newPosition = alternative.tryEat(line, position, parsingResult);
            if(newPosition != -1) {
                return newPosition;
            }
        }

        return -1;
    }
}
