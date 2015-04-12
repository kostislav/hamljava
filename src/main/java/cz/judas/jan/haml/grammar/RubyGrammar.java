package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.AtLeastOneToken;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.parser.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.StringRubyValue;
import cz.judas.jan.haml.tree.VariableReference;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHashEntry;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyValue;

import static cz.judas.jan.haml.parser.tokens.ReflectionToken.reference;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

@SuppressWarnings({"UtilityClass", "UnusedDeclaration"})
public class RubyGrammar {
    public static final SequenceOfTokens<MutableHtmlNode> HASH =
            sequence(
                    singleChar('{'),
                    GenericTokens.<MutableHtmlNode, MutableHash>contextSwitch(
                            MutableHash::new,
                            anyOf(
                                    hashEntries(reference("NEW_STYLE_HASH_ENTRY")),
                                    hashEntries(reference("OLD_STYLE_HASH_ENTRY"))
                            ),
                            MutableHtmlNode::addAttributes
                    ),
                    whitespace(),
                    singleChar('}')
            );

    private static AtLeastOneToken<? super MutableHash> hashEntries(Token<MutableHashEntry> token) {
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

    public static final WhitespaceAllowingSequenceToken<MutableHashEntry> NEW_STYLE_HASH_ENTRY =
            relaxedSequence(
                    match(atLeastOne(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to(MutableHashEntry::setName),
                    singleChar(':'),
                    whitespace(),
                    GenericTokens.<MutableHashEntry, MutableRubyValue>contextSwitch(
                            MutableRubyValue::new,
                            reference("VALUE"),
                            MutableHashEntry::setValue
                    )
            );

    public static final Token<MutableHashEntry> OLD_STYLE_HASH_ENTRY =
            relaxedSequence(
                    singleChar(':'),
                    match(atLeastOne(Predicates.TAG_NAME_CHAR), MutableHashEntry.class).to(MutableHashEntry::setName),
                    whitespace(),
                    exactText("=>"),
                    whitespace(),
                    GenericTokens.<MutableHashEntry, MutableRubyValue>contextSwitch(
                            MutableRubyValue::new,
                            reference("VALUE"),
                            MutableHashEntry::setValue
                    )
            );

    public static final Token<MutableRubyValue> VALUE =
            anyOf(
                    reference("VARIABLE"),
                    reference("SINGLE_QUOTE_VALUE")
            );

    public static final Token<MutableRubyValue> VARIABLE =
            sequence(
                    singleChar('@'),
                    match(atLeastOne(Predicates.TAG_NAME_CHAR), MutableRubyValue.class).to((mutableRubyValue, s) -> mutableRubyValue.setValue(new VariableReference(s)))
            );

    public static final Token<MutableRubyValue> SINGLE_QUOTE_VALUE =
            sequence(
                    singleChar('\''),
                    match(atLeastOne(Predicates.TAG_NAME_CHAR), MutableRubyValue.class).to((entry, value) -> entry.setValue(new StringRubyValue(value))),
                    singleChar('\'')
            );
}
