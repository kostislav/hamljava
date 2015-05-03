package cz.judas.jan.haml.parser.tokens.terminal;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.parser.tokens.Token;

import java.util.Optional;

public class EndOfLineToken implements Token<Optional<String>> {
    @Override
    public Optional<Optional<String>> tryEat(InputString line) {
        if (line.currentCharIs('\n')) {
            line.advance();
            return Optional.of(Optional.of("\n"));
        } else if (!line.hasMoreChars()) {
            return Optional.of(Optional.empty());
        } else {
            return Optional.empty();
        }
    }
}
