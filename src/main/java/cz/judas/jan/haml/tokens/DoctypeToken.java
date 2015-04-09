package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

public class DoctypeToken implements Token<MutableRootNode> {
    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        if(line.startsWith("!!!")) {
            if(line.startsWith("!!! 5")) {
                parsingResult.setDoctype("<!DOCTYPE html>");
                return 5;
            } else {
                throw new ParseException(line, 4);
            }
        } else {
            return -1;
        }
    }
}
