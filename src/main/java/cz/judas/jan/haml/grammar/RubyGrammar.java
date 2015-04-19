package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.FieldReference;
import cz.judas.jan.haml.tree.RubyString;
import cz.judas.jan.haml.tree.RubySymbol;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHashEntry;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

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
                sequence(
                        GenericTokens.<MutableHtmlNode, MutableHash>contextSwitch(
                                MutableHash::new,
                                GenericTokens.anyOf(
                                        hashEntries(newStyleHashEntry()),
                                        hashEntries(oldStyleHashEntry())
                                ),
                                (node, attributes) -> node.addAttributes(attributes.toHash())
                        ),
                        whitespace()
                ),
                '}'
        ));
    }

    private static Token<? super MutableHash> hashEntries(Token<MutableHashEntry> token) {
        return atLeastOne(
                relaxedSequence(
                        whitespace(),
                        GenericTokens.<MutableHash, MutableHashEntry>contextSwitch(
                                MutableHashEntry::new,
                                token,
                                (hash, entry) -> hash.addKeyValuePair(entry.toEntry())
                        ),
                        atMostOne(',')
                )
        );
    }

    private static Token<MutableHashEntry> newStyleHashEntry() {
        return rule(() -> relaxedSequence(
                whitespace(),
                newStyleHashKey(),
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        expression(),
                        (entry, value) -> entry.setValue(value.toExpression())
                )
        ));
    }

    private static Token<? super MutableHashEntry> newStyleHashKey() {
        return sequence(
                match(atLeastOneChar(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to((entry, name) -> entry.setKey(new RubySymbol(name))),
                singleChar(':')
        );
    }

    private static Token<MutableHashEntry> oldStyleHashEntry() {
        return rule(() -> relaxedSequence(
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
                )
        ));
    }

    public static Token<MutableRubyExpression> expression() {
        return rule(() -> anyOf(
                fieldReference(),
                singleQuoteString()
        ));
    }

    private static Token<MutableRubyExpression> fieldReference() {
        return rule(() -> sequence(
                singleChar('@'),
                match(variableName(), MutableRubyExpression.class).to((expression, name) -> expression.setValue(new FieldReference(name)))
        ));
    }

    public static Token<MutableRubyExpression> symbol() {
        return sequence(
                singleChar(':'),
                match(variableName(), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubySymbol(value)))
        );
    }

    public static Token<MutableRubyExpression> variableName() {
        return sequence(
                singleChar(c -> Character.isAlphabetic(c) || c == '$' || c == '_'),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')),
                anyNumberOf(singleChar(c -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '!' || c == '?' || c == '='))
        );
    }

    private static Token<MutableRubyExpression> singleQuoteString() {
        return rule(() -> delimited(
                '\'',
                match(atLeastOneChar(not(anyOfChars('\'', '\n'))), MutableRubyExpression.class).to((entry, value) -> entry.setValue(new RubyString(value))),
                '\''
        ));
    }
}
