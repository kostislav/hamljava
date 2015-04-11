package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.generic.SequenceOfTokens;
import cz.judas.jan.haml.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.mutable.MutableAttribute;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.terminal.Terminals.*;

public class DocumentToken implements Token<MutableRootNode> {
    private final WhitespaceAllowingSequenceToken<MutableAttribute> genericAttribute = relaxedSequence(
            whitespace(),
            match(atLeastOne(singleChar(new IsTagNameChar())), MutableAttribute.class).to(MutableAttribute::setName),
            singleChar(':'),
            whitespace(),
            singleChar('\''),
            match(atLeastOne(singleChar(new IsTagNameChar())), MutableAttribute.class).to(MutableAttribute::setValue),
            singleChar('\''),
            whitespace(),
            atMostOne(singleChar(','))
    );

    private final Token<MutableHtmlNode> idAttribute = leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass);

    private final Token<MutableHtmlNode> classAttribute = leadingChar('#', new IsIdOrClassChar(), MutableHtmlNode::setId);

    private final SequenceOfTokens<MutableHtmlNode> attributeHash = sequence(
            singleChar('{'),
            atLeastOne(
                    GenericTokens.<MutableHtmlNode, MutableAttribute>contextSwitch(
                            MutableAttribute::new,
                            genericAttribute,
                            MutableHtmlNode::addAttribute
                    )
            ),
            whitespace(),
            singleChar('}')
    );

    private final Token<MutableHtmlNode> tagName = leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName);

    private final Token<MutableRootNode> lines =
            anyNumberOf(
                    relaxedSequence(
                            anyOf(
                                    sequence(
                                            exactText("!!!"),
                                            whitespace(),
                                            match(atLeastOne(singleChar(Character::isLetterOrDigit)), MutableRootNode.class).to(MutableRootNode::setDoctype)
                                    ),
                                    sequence(
                                            match(anyNumberOf(singleChar('\t')), MutableRootNode.class).to(MutableRootNode::levelUp),
                                            GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                                                    MutableHtmlNode::new,
                                                    relaxedSequence(
                                                            atMostOne(tagName),
                                                            anyNumberOf(
                                                                    GenericTokens.<MutableHtmlNode>anyOf(
                                                                            strictWhitespace(),
                                                                            idAttribute,
                                                                            classAttribute,
                                                                            attributeHash
                                                                    )
                                                            ),
                                                            whitespace(),
                                                            match(anyNumberOf(singleChar(c -> c != '\n')), MutableHtmlNode.class).to(MutableHtmlNode::setContent)
                                                    ),
                                                    MutableRootNode::addNode
                                            )
                                    )
                            ),
                            atMostOne(singleChar('\n'))
                    )
            );

    @Override
    public int tryEat(String line, int position, MutableRootNode parsingResult) throws ParseException {
        return lines.tryEat(line, position, parsingResult);
    }
}
