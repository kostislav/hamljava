package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

public class AnyLineToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> options = new AnyOfToken<>(ImmutableList.of(
            new DoctypeToken(),
            new LineToken()
    ));

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return options.tryEat(line, position, parsingResult);
    }
}
