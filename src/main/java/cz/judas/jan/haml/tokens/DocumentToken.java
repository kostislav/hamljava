package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

public class DocumentToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> lines = new AnyNumberOfToken<>(
            new AnyLineToken()
    );

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return lines.tryEat(line, position, parsingResult);
    }
}
