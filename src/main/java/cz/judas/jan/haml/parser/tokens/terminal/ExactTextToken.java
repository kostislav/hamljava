package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;

public class ExactTextToken implements Token<String> {
    private final String content;

    public ExactTextToken(String content) {
        this.content = content;
    }

    @Override
    public Optional<String> tryEat(InputString line) {
        if(line.startsWith(content)) {
            return Optional.of(content);
        } else {
            return Optional.empty();
        }
    }
}
