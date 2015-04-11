package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SingleCharToken;
import cz.judas.jan.haml.tokens.generic.Terminals;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

public class DocumentToken implements Token<MutableRootNode> {
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
                                                                    Terminals.<MutableHtmlNode>leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName)
                                                            ),
                                                            anyNumberOf(
                                                                    anyOf(
                                                                            Terminals.<MutableHtmlNode>strictWhitespace(),
                                                                            Terminals.<MutableHtmlNode>leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass),
                                                                            Terminals.<MutableHtmlNode>leadingChar('#', new IsIdOrClassChar(), MutableHtmlNode::setId)
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
