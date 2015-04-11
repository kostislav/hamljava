package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.mutable.MutableAttribute;
import cz.judas.jan.haml.tree.mutable.MutableHash;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.anyOf;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.atMostOne;
import static cz.judas.jan.haml.tokens.terminal.Terminals.*;
import static cz.judas.jan.haml.tokens.terminal.Terminals.singleChar;
import static cz.judas.jan.haml.tokens.terminal.Terminals.whitespace;

@SuppressWarnings("UtilityClass")
public class HamlGrammar {
    public static Token<MutableRootNode> hamlDocument() {
        return anyNumberOf(
                relaxedSequence(
                        anyOf(
                                sequence(
                                        exactText("!!!"),
                                        whitespace(),
                                        match(atLeastOne(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype)
                                ),
                                sequence(
                                        match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                                        GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                                                MutableHtmlNode::new,
                                                anyOf(
                                                        sequence(
                                                                singleChar('\\'),
                                                                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to(MutableHtmlNode::setContent)
                                                        ),
                                                        relaxedSequence(
                                                                atMostOne(tagName()),
                                                                anyNumberOf(
                                                                        GenericTokens.<MutableHtmlNode>anyOf(
                                                                                strictWhitespace(),
                                                                                idAttribute(),
                                                                                classAttribute(),
                                                                                attributeHash()
                                                                        )
                                                                ),
                                                                whitespace(),
                                                                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to(MutableHtmlNode::setContent)
                                                        )
                                                ),
                                                MutableRootNode::addNode
                                        )
                                )
                        ),
                        atMostOne('\n')
                )
        );
    }

    private static Token<MutableHtmlNode> tagName() {
        return leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName);
    }

    private static Token<MutableHtmlNode> idAttribute() {
        return leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass);
    }

    private static Token<MutableHtmlNode> classAttribute() {
        return leadingChar('#', new IsIdOrClassChar(), MutableHtmlNode::setId);
    }

    private static WhitespaceAllowingSequenceToken<MutableAttribute> newStyleHashEntry() {
        return relaxedSequence(
                match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setName),
                singleChar(':'),
                whitespace(),
                value()
        );
    }

    private static WhitespaceAllowingSequenceToken<MutableAttribute> oldStyleHashEntry() {
        return relaxedSequence(
                singleChar(':'),
                match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setName),
                whitespace(),
                exactText("=>"),
                whitespace(),
                value()
        );
    }

    private static SequenceOfTokens<MutableAttribute> value() {
        return sequence(
                singleChar('\''),
                match(atLeastOne(new IsTagNameChar()), MutableAttribute.class).to(MutableAttribute::setValue),
                singleChar('\'')
        );
    }

    private static SequenceOfTokens<MutableHtmlNode> attributeHash() {
        return sequence(
                singleChar('{'),
                GenericTokens.<MutableHtmlNode, MutableHash>contextSwitch(
                        MutableHash::new,
                        atLeastOne(
                                relaxedSequence(
                                        whitespace(),
                                        GenericTokens.<MutableHash, MutableAttribute>contextSwitch(
                                                MutableAttribute::new,
                                                anyOf(
                                                        newStyleHashEntry(),
                                                        oldStyleHashEntry()
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
    }
}
