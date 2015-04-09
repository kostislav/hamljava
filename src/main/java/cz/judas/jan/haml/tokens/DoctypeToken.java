package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableNode;
import cz.judas.jan.haml.tree.Html5DoctypeNode;

public class DoctypeToken implements Token<MutableNode> {
    @Override
    public int tryEat(String line, int position, MutableNode parsingResult) throws ParseException {
        if(line.startsWith("!!!")) {
            if(line.equals("!!! 5")) {
                parsingResult.addChild(new Html5DoctypeNode());
                return line.length();
            } else {
                throw new ParseException("Unsupported doctype " + line.substring(4));
            }
        } else {
            return -1;
        }
    }
}
