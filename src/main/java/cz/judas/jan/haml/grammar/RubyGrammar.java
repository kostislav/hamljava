package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHashEntry;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

import java.util.List;
import java.util.Optional;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;
import static cz.judas.jan.haml.predicates.Predicates.anyOfChars;
import static cz.judas.jan.haml.predicates.Predicates.not;

@SuppressWarnings("UtilityClass")
public class RubyGrammar {
    public static TypedToken<MutableHtmlNode, RubyHash> hash() {
        return rule(() -> delimited(
                '{',
                GenericTokens.<MutableHtmlNode, List<HashEntry>, String, RubyHash>sequence(
                        GenericTokens.<MutableHtmlNode, MutableHash, List<HashEntry>>contextSwitch(
                                MutableHash::new,
                                GenericTokens.anyOf(
                                        hashEntries(newStyleHashEntry()),
                                        hashEntries(oldStyleHashEntry())
                                ),
                                (node, attributes) -> node.addAttributes(attributes.toHash())
                        ),
                        whitespace(),
                        (entries, ignored) -> new RubyHash(entries)
                ),
                '}'
        ));
    }

    private static TypedToken<MutableHash, List<HashEntry>> hashEntries(Token<MutableHashEntry> token) {
        return atLeastOne(
                GenericTokens.<MutableHash, String, HashEntry, Optional<Character>, HashEntry>relaxedSequence(
                        whitespace(),
                        GenericTokens.<MutableHash, MutableHashEntry, RubyHash>contextSwitch(
                                MutableHashEntry::new,
                                token,
                                (hash, entry) -> hash.addKeyValuePair(entry.toEntry())
                        ),
                        atMostOne(','),
                        (ignored1, entry, ignored2) -> entry
                )
        );
    }

    private static TypedToken<MutableHashEntry, HashEntry> newStyleHashEntry() {
        return rule(() -> GenericTokens.<MutableHashEntry, String, RubyExpression, RubyExpression, HashEntry>relaxedSequence(
                whitespace(),
                newStyleHashKey(),
                GenericTokens.<MutableHashEntry, MutableRubyExpression, HashEntry>contextSwitch(
                        MutableRubyExpression::new,
                        expression(),
                        (entry, value) -> entry.setValue(value.toExpression())
                ),
                (ignored, key, value) -> new HashEntry(key, value)
        ));
    }

    private static TypedToken<MutableHashEntry, RubySymbol> newStyleHashKey() {
        return rule(() -> GenericTokens.<MutableHashEntry, String, Character, String>sequence(
                match(atLeastOneChar(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to((entry, name) -> entry.setKey(new RubySymbol(name))),
                singleChar(':'),
                (key, ignored) -> key
        ));
    }

    private static TypedToken<MutableHashEntry, HashEntry> oldStyleHashEntry() {
        return rule(() -> GenericTokens.<MutableHashEntry, RubyExpression, String, RubyExpression, HashEntry>relaxedSequence(
                GenericTokens.<MutableHashEntry, MutableRubyExpression, RubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        symbol(),
                        (entry, name) -> entry.setKey(name.toExpression())
                ),
                exactText("=>"),
                GenericTokens.<MutableHashEntry, MutableRubyExpression, RubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        expression(),
                        (entry, value) -> entry.setValue(value.toExpression())
                ),
                (key, ignored, value) -> new HashEntry(key, value)
        ));
    }

    public static TypedToken<MutableRubyExpression, RubyExpression> expression() {
        return rule(() -> anyOf(
                fieldReference(),
                singleQuoteString()
        ));
    }

    private static TypedToken<MutableRubyExpression, FieldReference> fieldReference() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, FieldReference>sequence(
                singleChar('@'),
                match(variableName(), MutableRubyExpression.class).to((expression, name) -> expression.setValue(new FieldReference(name))),
                (ignored, name) ->new FieldReference(name)
        ));
    }

    public static TypedToken<MutableRubyExpression, RubySymbol> symbol() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, RubySymbol>sequence(
                singleChar(':'),
                match(variableName(), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubySymbol(value))),
                (ignored, name) -> new RubySymbol(name)
        ));
    }

    public static TypedToken<MutableRubyExpression, String> variableName() {
        return rule(() -> GenericTokens.<MutableRubyExpression, Character, String, String, String>sequence(
                singleChar(c -> Character.isAlphabetic(c) || c == '$' || c == '_'),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '!' || c == '?' || c == '=')),
                (firstChar, nextChars, lastChars) -> firstChar + nextChars + lastChars
        ));
    }

    private static TypedToken<MutableRubyExpression, RubyString> singleQuoteString() {
        return rule(() -> delimited(
                '\'',
                match(atLeastOneChar(not(anyOfChars('\'', '\n'))), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubyString(value))),
                '\''
        ));
    }
}
