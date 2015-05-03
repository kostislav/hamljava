package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.ruby.*;

import java.util.List;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;
import static cz.judas.jan.haml.predicates.Predicates.anyOfChars;
import static cz.judas.jan.haml.predicates.Predicates.not;

public class RubyGrammar {
    public Token<RubyHash> hash() {
        return rule(() -> delimited(
                '{',
                sequence(
                        GenericTokens.anyOf(
                                hashEntries(newStyleHashEntry()),
                                hashEntries(oldStyleHashEntry())
                        ),
                        whitespace(),
                        (entries, ignored) -> new RubyHash(entries)
                ),
                '}'
        ));
    }

    private Token<List<HashEntry>> hashEntries(Token<HashEntry> token) {
        return atLeastOne(
                relaxedSequence(
                        whitespace(),
                        token,
                        atMostOne(','),
                        (ignored1, entry, ignored2) -> entry
                )
        );
    }

    private Token<HashEntry> newStyleHashEntry() {
        return rule(() -> relaxedSequence(
                whitespace(),
                newStyleHashKey(),
                expression(),
                (ignored, key, value) -> new HashEntry(key, value)
        ));
    }

    private Token<RubySymbol> newStyleHashKey() {
        return rule(() -> sequence(
                atLeastOneChar(Predicates.TAG_NAME_CHAR),
                singleChar(':'),
                (key, ignored) -> new RubySymbol(key)
        ));
    }

    private Token<HashEntry> oldStyleHashEntry() {
        return rule(() -> relaxedSequence(
                symbol(),
                exactText("=>"),
                expression(),
                (key, ignored, value) -> new HashEntry(key, value)
        ));
    }

    public Token<RubyExpression> expression() {
        return rule(() -> anyOf(
                methodCall(),
                fieldReference(),
                singleQuoteString()
        ));
    }

    private Token<MethodCall> methodCall() {
        return rule(() -> sequence(
                fieldReference(),
                singleChar('.'),
                variableName(),
                (field, ignored, methodName) -> new MethodCall(field.getName(), methodName)
        ));
    }

    private Token<FieldReference> fieldReference() {
        return rule(() -> sequence(
                singleChar('@'),
                variableName(),
                (ignored, name) -> new FieldReference(name)
        ));
    }

    public Token<RubySymbol> symbol() {
        return rule(() -> sequence(
                singleChar(':'),
                variableName(),
                (ignored, name) -> new RubySymbol(name)
        ));
    }

    public Token<String> variableName() {
        return rule(() -> sequence(
                singleChar(c -> Character.isAlphabetic(c) || c == '$' || c == '_'),
                anyNumberOfChars(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_'),
                anyNumberOfChars(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '!' || c == '?' || c == '='),
                (firstChar, nextChars, lastChars) -> firstChar + nextChars + lastChars
        ));
    }

    private Token<RubyString> singleQuoteString() {
        return rule(() -> transformation(
                delimited(
                        '\'',
                        atLeastOneChar(not(anyOfChars('\'', '\n'))),
                        '\''
                ),
                RubyString::new
        ));
    }
}
