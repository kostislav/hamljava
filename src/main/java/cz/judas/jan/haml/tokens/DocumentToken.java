package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableAttribute;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.tokens.generic.Terminals;
import cz.judas.jan.haml.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.generic.Terminals.leadingChar;

public class DocumentToken implements Token<MutableRootNode> {
    private final WhitespaceAllowingSequenceToken<MutableAttribute> genericAttribute = relaxedSequence(
            Terminals.<MutableAttribute>whitespace(),
            GenericTokens.<MutableAttribute>onMatch(
                    atLeastOne(
                            Terminals.singleChar(new IsTagNameChar())
                    ),
                    MutableAttribute::setName
            ),
            Terminals.<MutableAttribute>singleChar(':'),
            Terminals.<MutableAttribute>whitespace(),
            Terminals.<MutableAttribute>singleChar('\''),
            GenericTokens.<MutableAttribute>onMatch(
                    atLeastOne(
                            Terminals.singleChar(new IsTagNameChar())
                    ),
                    MutableAttribute::setValue
            ),
            Terminals.<MutableAttribute>singleChar('\''),
            Terminals.<MutableAttribute>whitespace(),
            atMostOne(Terminals.<MutableAttribute>singleChar(','))
    );

    private final Token<MutableHtmlNode> idAttribute = leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass);

    private final Token<MutableHtmlNode> classAttribute = leadingChar('#', new IsIdOrClassChar(), MutableHtmlNode::setId);

    private final SequenceOfTokens<MutableHtmlNode> attributeHash = sequence(
            Terminals.<MutableHtmlNode>singleChar('{'),
            atLeastOne(
                    GenericTokens.<MutableHtmlNode, MutableAttribute>contextSwitch(
                            MutableAttribute::new,
                            genericAttribute,
                            MutableHtmlNode::addAttribute
                    )
            ),
            Terminals.<MutableHtmlNode>whitespace(),
            Terminals.<MutableHtmlNode>singleChar('}')
    );

    private final Token<MutableHtmlNode> tagName = leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName);

    private final Token<MutableRootNode> lines =
            anyNumberOf(
                    relaxedSequence(
                            anyOf(
                                    new DoctypeToken(),
                                    sequence(
                                            new SignificantWhitespaceToken(),
                                            GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                                                    MutableHtmlNode::new,
                                                    relaxedSequence(
                                                            atMostOne(
                                                                    tagName
                                                            ),
                                                            anyNumberOf(
                                                                    anyOf(
                                                                            Terminals.<MutableHtmlNode>strictWhitespace(),
                                                                            idAttribute,
                                                                            classAttribute,
                                                                            attributeHash
                                                                    )
                                                            ),
                                                            Terminals.<MutableHtmlNode>whitespace(),
                                                            atMostOne(
                                                                    new TextToken()
                                                            )
                                                    ),
                                                    MutableRootNode::addNode
                                            )
                                    )
                            ),
                            atMostOne(
                                    Terminals.<MutableRootNode>singleChar('\n')
                            )
                    )
            );

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return lines.tryEat(line, position, parsingResult);
    }
}
