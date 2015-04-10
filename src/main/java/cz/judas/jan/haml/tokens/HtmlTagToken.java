package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.ContextSwitchToken;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

public class HtmlTagToken implements Token<MutableRootNode> {
    private final Token<MutableRootNode> innerTokens = new ContextSwitchToken<>(
            MutableHtmlNode::new,
            sequence(
                    atMostOne(
                            new LeadingCharToken('%', this::isTagNameChar, MutableHtmlNode::setTagName)
                    ),
                    anyNumberOf(
                            anyOf(
                                    new LeadingCharToken('.', this::isIdOrClassChar, MutableHtmlNode::addClass),
                                    new LeadingCharToken('#', this::isIdOrClassChar, MutableHtmlNode::setId)
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

    private boolean isIdOrClassChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
    }

    private boolean isTagNameChar(char c) {
        return Character.isLetterOrDigit(c);
    }
}
