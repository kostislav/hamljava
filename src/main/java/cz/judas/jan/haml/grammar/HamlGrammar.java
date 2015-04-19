package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.RubyString;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar implements Grammar<MutableRootNode> {

    @Override
    public Token<MutableRootNode> buildRules() {
        return sequence(
                atMostOne(line(doctype())),
                anyNumberOf(line(indentedLine()))
        );
    }

    private static Token<MutableRootNode> doctype() {
        return rule(() -> sequence(
                exactText("!!!"),
                whitespace(),
                match(atLeastOneChar(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype)
        ));
    }

    private static Token<MutableRootNode> indentedLine() {
        return rule(() -> sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                        MutableHtmlNode::new,
                        lineContent(),
                        MutableRootNode::addNode
                )
        ));
    }

    private static Token<MutableHtmlNode> lineContent() {
        return anyOf(
                escapedPlainText(),
                printExpression(),
                htmlTag()
        );
    }

    private static Token<MutableHtmlNode> escapedPlainText() {
        return rule(() -> sequence(
                singleChar('\\'),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new RubyString(value)))
        ));
    }

    private static Token<MutableHtmlNode> printExpression() {
        return rule(() -> relaxedSequence(
                singleChar('='),
                GenericTokens.<MutableHtmlNode, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
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
                                idAttribute(),
                                classAttribute(),
                                RubyGrammar.hash()
                        )
                ),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new RubyString(value)))
        ));
    }

    private static Token<MutableHtmlNode> tagName() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('%', Predicates.TAG_NAME_CHAR, MutableHtmlNode::setTagName));
    }

    private static Token<MutableHtmlNode> idAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('#', Predicates.ID_OR_CLASS_CHAR, (node, value) -> node.setId(new RubyString(value))));
    }

    private static Token<MutableHtmlNode> classAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode>leadingChar('.', Predicates.ID_OR_CLASS_CHAR, MutableHtmlNode::addClass));
    }
}
