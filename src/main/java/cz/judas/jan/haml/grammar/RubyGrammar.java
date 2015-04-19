package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHashEntry;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

import java.util.Optional;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;
import static cz.judas.jan.haml.predicates.Predicates.anyOfChars;
import static cz.judas.jan.haml.predicates.Predicates.not;

@SuppressWarnings("UtilityClass")
public class RubyGrammar {
    public static Token<MutableHtmlNode> hash() {
        return rule(() -> delimited(
                '{',
                GenericTokens.<MutableHtmlNode, RubyHash, String, RubyHash>sequence(
                        GenericTokens.<MutableHtmlNode, MutableHash>contextSwitch(
                                MutableHash::new,
                                GenericTokens.anyOf(
                                        hashEntries(newStyleHashEntry()),
                                        hashEntries(oldStyleHashEntry())
                                ),
                                (node, attributes) -> node.addAttributes(attributes.toHash())
                        ),
                        whitespace(),
                        (hash, ignored) -> hash
                ),
                '}'
        ));
    }

    private static Token<? super MutableHash> hashEntries(Token<MutableHashEntry> token) {
        return atLeastOne(
                GenericTokens.<MutableHash, String, HashEntry, Optional<Character>, HashEntry>relaxedSequence(
                        whitespace(),
                        GenericTokens.<MutableHash, MutableHashEntry>contextSwitch(
                                MutableHashEntry::new,
                                token,
                                (hash, entry) -> hash.addKeyValuePair(entry.toEntry())
                        ),
                        atMostOne(','),
                        (ignored1, entry, ignored2) -> entry
                )
        );
    }

    private static Token<MutableHashEntry> newStyleHashEntry() {
        return rule(() -> GenericTokens.<MutableHashEntry, String, RubyExpression, RubyExpression, HashEntry>relaxedSequence(
                whitespace(),
                newStyleHashKey(),
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        expression(),
                        (entry, value) -> entry.setValue(value.toExpression())
                ),
                (ignored, key, value) -> new HashEntry(key, value)
        ));
    }

    private static Token<? super MutableHashEntry> newStyleHashKey() {
        return rule(() -> GenericTokens.<MutableHashEntry, String, Character, String>sequence(
                match(atLeastOneChar(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to((entry, name) -> entry.setKey(new RubySymbol(name))),
                singleChar(':'),
                (key, ignored) -> key
        ));
    }

    private static Token<MutableHashEntry> oldStyleHashEntry() {
        return rule(() -> GenericTokens.<MutableHashEntry, RubyExpression, String, RubyExpression, HashEntry>relaxedSequence(
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        symbol(),
                        (entry, name) -> entry.setKey(name.toExpression())
                ),
                exactText("=>"),
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        expression(),
                        (entry, value) -> entry.setValue(value.toExpression())
                ),
                (key, ignored, value) -> new HashEntry(key, value)
        ));
    }

    public static Token<MutableRubyExpression> expression() {
        return rule(() -> anyOf(
                fieldReference(),
                singleQuoteString()
        ));
    }

    private static Token<MutableRubyExpression> fieldReference() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, FieldReference>sequence(
                singleChar('@'),
                match(variableName(), MutableRubyExpression.class).to((expression, name) -> expression.setValue(new FieldReference(name))),
                (ignored, name) ->new FieldReference(name)
        ));
    }

    public static Token<MutableRubyExpression> symbol() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, RubySymbol>sequence(
                singleChar(':'),
                match(variableName(), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubySymbol(value))),
                (ignored, name) -> new RubySymbol(name)
        ));
    }

    public static Token<MutableRubyExpression> variableName() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, String, String>sequence(
                singleChar(c -> Character.isAlphabetic(c) || c == '$' || c == '_'),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '!' || c == '?' || c == '=')),
                (firstChar, nextChars, lastChars) -> firstChar + nextChars + lastChars
        ));
    }

    private static Token<MutableRubyExpression> singleQuoteString() {
        return rule(() -> delimited(
                '\'',
                match(atLeastOneChar(not(anyOfChars('\'', '\n'))), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubyString(value))),
                '\''
        ));
    }
}
