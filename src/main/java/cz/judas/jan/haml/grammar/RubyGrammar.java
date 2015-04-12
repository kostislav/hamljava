package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.mutable.MutableAttribute;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;

import static cz.judas.jan.haml.tokens.ReflectionToken.reference;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.atLeastOne;
import static cz.judas.jan.haml.tokens.terminal.Terminals.exactText;
import static cz.judas.jan.haml.tokens.terminal.Terminals.singleChar;
import static cz.judas.jan.haml.tokens.terminal.Terminals.whitespace;

@SuppressWarnings({"UtilityClass", "UnusedDeclaration"})
public class RubyGrammar {
    public static final SequenceOfTokens<MutableHtmlNode> HASH = sequence(
            singleChar('{'),
            GenericTokens.<MutableHtmlNode, MutableHash>contextSwitch(
                    MutableHash::new,
                    atLeastOne(
                            relaxedSequence(
                                    whitespace(),
                                    GenericTokens.<MutableHash, MutableAttribute>contextSwitch(
                                            MutableAttribute::new,
                                            anyOf(
                                                    reference("NEW_STYLE_HASH_ENTRY"),
                                                    reference("OLD_STYLE_HASH_ENTRY")
                                            ),
                                            MutableHash::addKeyValuePair
                                    ),
                                    atMostOne(',')
                            )
                    ),
                    MutableHtmlNode::addAttributes
            ),
            whitespace(),
            singleChar('}')
    );

    public static final WhitespaceAllowingSequenceToken<MutableAttribute> NEW_STYLE_HASH_ENTRY = relaxedSequence(
            match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setName),
            singleChar(':'),
            whitespace(),
            reference("SINGLE_QUOTE_VALUE")
    );

    public static final Token<MutableAttribute> OLD_STYLE_HASH_ENTRY = relaxedSequence(
            singleChar(':'),
            match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setName),
            whitespace(),
            exactText("=>"),
            whitespace(),
            reference("SINGLE_QUOTE_VALUE")
    );

    public static final Token<MutableAttribute> SINGLE_QUOTE_VALUE = sequence(
            singleChar('\''),
            match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setValue),
            singleChar('\'')
    );
}
