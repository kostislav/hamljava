package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.ContextSwitchToken;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

public class HtmlTagToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> innerTokens = new ContextSwitchToken<>(
            MutableHtmlNode::new,
            sequence(
                    atMostOne(
                            new LeadingCharToken('%', new IsTagNameChar(), MutableHtmlNode::setTagName)
                    ),
                    anyNumberOf(
                            anyOf(
                                    new LeadingCharToken('.', new IsIdOrClassChar(), MutableHtmlNode::addClass),
                                    new LeadingCharToken('#', new IsIdOrClassChar(), MutableHtmlNode::setId)
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
    );

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return innerTokens.tryEat(line, position, parsingResult);
    }
}
