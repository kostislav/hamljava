package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.TypedToken;

public class ExactTextToken implements TypedToken<Object, String> {
    private final String content;

    public ExactTextToken(String content) {
        this.content = content;
    }

    @Override
    public boolean tryEat(InputString line, Object parsingResult) {
        return line.startsWith(content);
    }
}
