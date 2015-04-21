package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;

public class PrecededWithWhitespaceToken<C, T> implements TypedToken<C, T> {
    private final Token<? super C> whitespace;
    private final Token<? super C> token;

    public PrecededWithWhitespaceToken(Token<? super C> token) {
        whitespace = Terminals.whitespace();
        this.token = token;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(inputString -> whitespace.tryEat(inputString, parsingResult) && token.tryEat(inputString, parsingResult));
    }
}
