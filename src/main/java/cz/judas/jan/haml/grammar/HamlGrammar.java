package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.StringRubyValue;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyValue;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar implements Grammar<MutableRootNode> {

    @Override
    public Token<MutableRootNode> buildRules() {
        return anyNumberOf(
                relaxedSequence(
                        anyOf(
                                doctype(),
                                regularLine()
                        ),
                        atMostOne('\n')
                )
        );
    }

    private static Token<MutableRootNode> doctype() {
        return rule(() -> sequence(
                exactText("!!!"),
                whitespace(),
                match(atLeastOneChar(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype)
        ));
    }

    private static Token<MutableRootNode> regularLine() {
        return rule(() -> sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                        MutableHtmlNode::new,
                        anyOf(
                                escapedPlainText(),
                                printExpression(),
                                htmlTag()
                        ),
                        MutableRootNode::addNode
                )
        ));
    }

    private static Token<MutableHtmlNode> escapedPlainText() {
        return rule(() -> sequence(
                singleChar('\\'),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new StringRubyValue(value)))
        ));
    }

    private static Token<MutableHtmlNode> printExpression() {
        return rule(() -> relaxedSequence(
                singleChar('='),
                whitespace(),
                GenericTokens.<MutableHtmlNode, MutableRubyValue>contextSwitch(
                        MutableRubyValue::new,
                        RubyGrammar.value(),
                        (node, value) -> node.setContent(value.getValue())
                )
        ));
    }

    private static Token<MutableHtmlNode> htmlTag() {
        return rule(() -> relaxedSequence(
                atMostOne(tagName()),
                anyNumberOf(
                        GenericTokens.<MutableHtmlNode>anyOf(
                                strictWhitespace(),
                                idAttribute(),
                                classAttribute(),
                                RubyGrammar.hash()
                        )
                ),
                whitespace(),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new StringRubyValue(value)))
        ));
    }

    private static Token<MutableHtmlNode> tagName() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('%', Predicates.TAG_NAME_CHAR, MutableHtmlNode::setTagName));
    }

    private static Token<MutableHtmlNode> idAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('#', Predicates.ID_OR_CLASS_CHAR, (node, value) -> node.setId(new StringRubyValue(value))));
    }

    private static Token<MutableHtmlNode> classAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('.', Predicates.ID_OR_CLASS_CHAR, MutableHtmlNode::addClass));
    }
}
