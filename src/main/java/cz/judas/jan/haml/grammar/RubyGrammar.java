package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.RubyString;
import cz.judas.jan.haml.tree.FieldReference;
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
                                MutableHtmlNode::addAttributes
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
                                MutableHash::addKeyValuePair
                        ),
                        atMostOne(',')
                )
        );
    }

    private static Token<MutableHashEntry> newStyleHashEntry() {
        return rule(() -> sequence(
                match(atLeastOneChar(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to(MutableHashEntry::setName),
                singleChar(':'),
                whitespace(),
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        value(),
                        MutableHashEntry::setValue
                )
        ));
    }

    private static Token<MutableHashEntry> oldStyleHashEntry() {
        return rule(() -> sequence(
                singleChar(':'),
                match(atLeastOneChar(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to(MutableHashEntry::setName),
                whitespace(),
                exactText("=>"),
                whitespace(),
                GenericTokens.<MutableHashEntry, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        value(),
                        MutableHashEntry::setValue
                )
        ));
    }

    public static Token<MutableRubyExpression> value() {
        return rule(() -> anyOf(
                variable(),
                singleQuoteString()
        ));
    }

    private static Token<MutableRubyExpression> variable() {
        return rule(() -> leadingChar(
                '@',
                Predicates.TAG_NAME_CHAR,
                (mutableRubyValue, s) -> mutableRubyValue.setValue(new FieldReference(s))
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
