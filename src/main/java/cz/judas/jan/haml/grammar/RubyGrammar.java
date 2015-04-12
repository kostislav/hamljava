package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tokens.generic.AtLeastOneToken;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.mutable.MutableHashEntry;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;

import static cz.judas.jan.haml.tokens.ReflectionToken.reference;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.terminal.Terminals.exactText;
import static cz.judas.jan.haml.tokens.terminal.Terminals.singleChar;
import static cz.judas.jan.haml.tokens.terminal.Terminals.whitespace;

@SuppressWarnings({"UtilityClass", "UnusedDeclaration"})
public class RubyGrammar {
    public static final SequenceOfTokens<MutableHtmlNode> HASH = sequence(
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

    public static final WhitespaceAllowingSequenceToken<MutableHashEntry> NEW_STYLE_HASH_ENTRY = relaxedSequence(
            match(atLeastOne(new IsTagNameChar()), MutableHashEntry.class).to(MutableHashEntry::setName),
            singleChar(':'),
            whitespace(),
            reference("SINGLE_QUOTE_VALUE")
    );

    public static final Token<MutableHashEntry> OLD_STYLE_HASH_ENTRY = relaxedSequence(
            singleChar(':'),
            match(atLeastOne(new IsTagNameChar()), MutableHashEntry.class).to(MutableHashEntry::setName),
            whitespace(),
            exactText("=>"),
            whitespace(),
            reference("SINGLE_QUOTE_VALUE")
    );

    public static final Token<MutableHashEntry> SINGLE_QUOTE_VALUE = sequence(
            singleChar('\''),
            match(atLeastOne(new IsTagNameChar()), MutableHashEntry.class).to(MutableHashEntry::setValue),
            singleChar('\'')
    );
}
