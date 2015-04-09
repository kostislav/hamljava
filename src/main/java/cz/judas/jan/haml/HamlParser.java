package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.*;
import cz.judas.jan.haml.tree.Node;

import static cz.judas.jan.haml.tokens.SequenceOfTokens.sequence;

public class HamlParser {
    private final Token<MutableRootNode> doctypeToken = new DoctypeToken();
    private final Token<MutableRootNode> hyperToken = sequence(ImmutableList.of(
            new SignificantWhitespaceToken(),
            new HtmlTagToken()
    ));

    public String process(String haml) throws ParseException {
        MutableRootNode rootNode = new MutableRootNode();

        for (String line : haml.split("\n")) {
            if(doctypeToken.tryEat(line, 0, rootNode) == -1) {
                hyperToken.tryEat(line, 0, rootNode);
            }
        }

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }
}
