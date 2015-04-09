package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.AnyOfToken;
import cz.judas.jan.haml.tokens.generic.AtMostOneToken;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;

public class AnyLineToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> options = new SequenceOfTokens<>(ImmutableList.of(
            new AnyOfToken<>(ImmutableList.of(
                    new DoctypeToken(),
                    new LineToken()
            )),
            new AtMostOneToken<>(
                new NewLineToken<MutableRootNode>()
            )
    ));

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return options.tryEat(line, position, parsingResult);
    }
}
