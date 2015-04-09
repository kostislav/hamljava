package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

public class SignificantWhitespaceToken implements Token<MutableRootNode> {
    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        int numTabs = leadingTabs(line, position);

        while (numTabs < parsingResult.nestingLevel()) { // TODO move
            parsingResult.levelUp();
        }
        return position + numTabs;
    }

    private int leadingTabs(String line, int startPos) {
        int numTabs = 0;
        while (line.charAt(startPos + numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }
}
