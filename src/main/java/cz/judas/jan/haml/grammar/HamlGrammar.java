package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.generic.WhitespaceAllowingSequenceToken;
import cz.judas.jan.haml.parser.tokens.predicates.IsIdOrClassChar;
import cz.judas.jan.haml.parser.tokens.predicates.IsTagNameChar;
import cz.judas.jan.haml.tree.StringRubyValue;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyValue;

import static cz.judas.jan.haml.parser.tokens.ReflectionToken.reference;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

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
                                    reference("ESCAPED_PLAINTEXT"),
                                    reference("PRINT_EXPRESSION"),
                                    reference("HTML_TAG")
                            ),
                            MutableRootNode::addNode
                    )
            );

    private static final Token<MutableHtmlNode> ESCAPED_PLAINTEXT =
            sequence(
                    singleChar('\\'),
                    match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new StringRubyValue(value)))
            );

    private static final WhitespaceAllowingSequenceToken<MutableHtmlNode> PRINT_EXPRESSION =
            relaxedSequence(
                    singleChar('='),
                    whitespace(),
                    GenericTokens.<MutableHtmlNode, MutableRubyValue>contextSwitch(
                            MutableRubyValue::new,
                            RubyGrammar.VALUE,
                            (node, value) -> node.setContent(value.getValue())
                    )
            );

    private static final Token<MutableHtmlNode> HTML_TAG = relaxedSequence(
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
            match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new StringRubyValue(value)))
    );

    private static final Token<MutableHtmlNode> TAG_NAME =
            leadingChar('%', new IsTagNameChar(), MutableHtmlNode::setTagName);

    private static final Token<MutableHtmlNode> ID_ATTRIBUTE =
            leadingChar('#', new IsIdOrClassChar(), (node, value) -> node.setId(new StringRubyValue(value)));

    private static final Token<MutableHtmlNode> CLASS_ATTRIBUTE =
            leadingChar('.', new IsIdOrClassChar(), MutableHtmlNode::addClass);
}
