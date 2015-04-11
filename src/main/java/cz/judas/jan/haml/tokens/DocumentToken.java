package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

public class DocumentToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> lines =
            anyNumberOf(
                    sequence(
                            anyOf(
                                    new DoctypeToken(),
                                    sequence(
                                            new SignificantWhitespaceToken(),
                                            GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                                                    MutableHtmlNode::new,
                                                    sequence(
                                                            atMostOne(
                                                                    new LeadingCharToken<MutableHtmlNode>('%', new IsTagNameChar(), MutableHtmlNode::setTagName)
                                                            ),
                                                            anyNumberOf(
                                                                    anyOf(
                                                                            new LeadingCharToken<MutableHtmlNode>('.', new IsIdOrClassChar(), MutableHtmlNode::addClass),
                                                                            new LeadingCharToken<MutableHtmlNode>('#', new IsIdOrClassChar(), MutableHtmlNode::setId)
                                                                    )
                                                            ),
                                                            anyNumberOf(
                                                                    new SingleCharToken<MutableHtmlNode>(' ')
                                                            ),
                                                            atMostOne(
                                                                    new TextToken()
                                                            )
                                                    ),
                                                    MutableRootNode::addNode
                                            )
                                    )
                            ),
                            atMostOne(
                                    new SingleCharToken<MutableRootNode>('\n')
                            )
                    )
            );

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return lines.tryEat(line, position, parsingResult);
    }
}
