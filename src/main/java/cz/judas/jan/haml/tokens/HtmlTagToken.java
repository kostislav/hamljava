package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;

public class HtmlTagToken implements Token<MutableRootNode> {
    private final Token<MutableHtmlNode> innerTokens = sequence(ImmutableList.of(
            atMostOne(
                    new LeadingCharToken(
                            '%',
                            this::isTagNameChar,
                            MutableHtmlNode::setTagName
                    )
            ),
            anyNumberOf(
                    anyOf(ImmutableList.of(
                            new LeadingCharToken(
                                    '.',
                                    this::isIdOrClassChar,
                                    MutableHtmlNode::addClass
                            ),
                            new LeadingCharToken(
                                    '#',
                                    this::isIdOrClassChar,
                                    MutableHtmlNode::setId
                            )
                    ))
            ),
            atMostOne(
                    new LeadingCharToken(
                            ' ',
                            c -> c != '\n',
                            MutableHtmlNode::setContent
                    )
            )
    ));

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        MutableHtmlNode mutableHtmlNode = new MutableHtmlNode();
        int newPosition = innerTokens.tryEat(line, position, mutableHtmlNode);
        if(newPosition != -1) {
            parsingResult.addNode(mutableHtmlNode);
        }
        return newPosition;
    }

    private boolean isIdOrClassChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
    }

    private boolean isTagNameChar(char c) {
        return Character.isLetterOrDigit(c);
    }
}
