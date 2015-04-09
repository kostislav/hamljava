package cz.judas.jan.haml.tokens;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.generic.AnyNumberOfToken;
import cz.judas.jan.haml.tokens.generic.AnyOfToken;
import cz.judas.jan.haml.tokens.generic.AtMostOneToken;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;

public class HtmlTagToken implements Token<MutableRootNode> {
    private final Token<MutableHtmlNode> innerTokens = new SequenceOfTokens<>(ImmutableList.of(
            new AtMostOneToken<>(
                    new LeadingCharToken(
                            '%',
                            this::isTagNameChar,
                            MutableHtmlNode::setTagName
                    )
            ),
            new AnyNumberOfToken<>(
                    new AnyOfToken<>(ImmutableList.of(
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
            new AtMostOneToken<>(
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
