package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

public class DoctypeToken implements Token<MutableRootNode> {
    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        if(line.startsWith("!!!")) {
            if(line.equals("!!! 5")) {
                parsingResult.setDoctype("<!DOCTYPE html>");
                return line.length();
            } else {
                throw new ParseException("Unsupported doctype " + line.substring(4));
            }
        } else {
            return -1;
        }
    }
}
