package cz.judas.jan.haml.parser.tokens.generic;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;

public class PrecededWithWhitespaceToken<C, T> implements TypedToken<C, T> {
    private final TypedToken<? super C, String> whitespace;
    private final TypedToken<? super C, ? extends T> token;

    public PrecededWithWhitespaceToken(TypedToken<? super C, ? extends T> token) {
        whitespace = Terminals.whitespace();
        this.token = token;
    }

    @Override
    public boolean tryEat(InputString line, C parsingResult) {
        return line.tryParse(inputString -> whitespace.tryEat(inputString, parsingResult) && token.tryEat(inputString, parsingResult));
    }
}
