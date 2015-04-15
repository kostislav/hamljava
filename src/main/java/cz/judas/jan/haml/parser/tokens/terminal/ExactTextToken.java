package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

public class ExactTextToken implements Token<Object> {
    private final String content;

    public ExactTextToken(String content) {
        this.content = content;
    }

    @Override
    public int tryEat(String line, int position, Object parsingResult) {
        return tryEat(new InputString(line, position), parsingResult);
    }

    public int tryEat(InputString line, Object parsingResult) {
        if(line.startsWith(content)) {
            return line.currentPosition();
        } else {
            return -1;
        }
    }
}
