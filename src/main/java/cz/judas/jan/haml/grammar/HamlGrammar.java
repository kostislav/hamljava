package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tokens.generic.GenericTokens;
import cz.judas.jan.haml.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.StringRubyValue;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import static cz.judas.jan.haml.tokens.ReflectionToken.reference;
import static cz.judas.jan.haml.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.tokens.terminal.Terminals.*;

@SuppressWarnings({"UtilityClass", "UnusedDeclaration"})
public class HamlGrammar {

    public static Token<MutableRootNode> hamlDocument() {
        return anyNumberOf(
                relaxedSequence(
                        anyOf(
                                reference("DOCTYPE"),
                                reference("REGULAR_LINE")
                        ),
                        atMostOne('\n')
                )
        );
    }

    private static final Token<MutableRootNode> DOCTYPE =
        sequence(
                exactText("!!!"),
                whitespace(),
                match(atLeastOne(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype)
        );

    private static final Token<MutableRootNode> REGULAR_LINE =
        sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                        MutableHtmlNode::new,
                        anyOf(
                                reference("ESCAPED_LINE"),
                                relaxedSequence(
                                        atMostOne(reference("TAG_NAME")),
                                        anyNumberOf(
                                                GenericTokens.<MutableHtmlNode>anyOf(
                                                        strictWhitespace(),
                                                        reference("ID_ATTRIBUTE"),
                                                        reference("CLASS_ATTRIBUTE"),
                                                        RubyGrammar.HASH
                                                )
                                        ),
                                        whitespace(),
                                        match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to(MutableHtmlNode::setContent)
                                )
                        ),
                        MutableRootNode::addNode
                )
        );

    private static final Token<MutableHtmlNode> ESCAPED_LINE =
            sequence(
                    singleChar('\\'),
                    match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to(MutableHtmlNode::setContent)
            );

    private static final Token<MutableHtmlNode> TAG_NAME =
            leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName);

    private static final Token<MutableHtmlNode> ID_ATTRIBUTE =
            leadingChar('#', new IsIdOrClassChar(), (node, value) -> node.setId(new StringRubyValue(value)));

    private static final Token<MutableHtmlNode> CLASS_ATTRIBUTE =
            leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass);
}
