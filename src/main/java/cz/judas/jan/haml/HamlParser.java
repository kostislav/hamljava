package cz.judas.jan.haml;

import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.DoctypeToken;
import cz.judas.jan.haml.tokens.HtmlTagToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.Node;

public class HamlParser {
    private final Token<MutableRootNode> doctypeToken = new DoctypeToken();
    private final Token<MutableRootNode> tagToken = new HtmlTagToken();

    public String process(String haml) throws ParseException {
        MutableRootNode rootNode = new MutableRootNode();

        for (String line : haml.split("\n")) {
            if(doctypeToken.tryEat(line, 0, rootNode) == -1) {
                int numTabs = leadingTabs(line);

                while (numTabs < rootNode.nestingLevel()) {
                    rootNode.levelUp();
                }

                tagToken.tryEat(line, numTabs, rootNode);
            }
        }

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while (line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }
}
