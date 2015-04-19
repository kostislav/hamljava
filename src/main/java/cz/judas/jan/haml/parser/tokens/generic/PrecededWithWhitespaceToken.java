package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;

public class PrecededWithWhitespaceToken<T> implements Token<T> {
    private final Token<? super T> whitespace;
    private final Token<? super T> token;

    public PrecededWithWhitespaceToken(Token<? super T> token) {
        whitespace = Terminals.whitespace();
        this.token = token;
    }

    @Override
    public boolean tryEat(InputString line, T parsingResult) {
        return line.tryParse(inputString -> whitespace.tryEat(inputString, parsingResult) && token.tryEat(inputString, parsingResult));
    }
}
