package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

import static cz.judas.jan.haml.tokens.generic.SequenceOfTokens.sequence;

public class LineToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> tokens = sequence(ImmutableList.of(
            new SignificantWhitespaceToken(),
            new HtmlTagToken()
    ));

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return tokens.tryEat(line, position, parsingResult);
    }
}
