package cz.judas.jan.haml;

import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

public class DocumentToken implements Token<MutableRootNode> {
    private static final Token<MutableRootNode> TOKEN = HamlGrammar.hamlDocument();

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return TOKEN.tryEat(line, position, parsingResult);
    }
}
