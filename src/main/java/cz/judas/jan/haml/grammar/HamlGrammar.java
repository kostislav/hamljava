package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

import java.util.List;
import java.util.Optional;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar implements Grammar<MutableRootNode> {

    @Override
    public Token<MutableRootNode> buildRules() {
        return GenericTokens.<MutableRootNode, Optional<String>, List<Node>, RootNode>sequence(
                atMostOne(line(doctype())),
                anyNumberOf(line(indentedLine())),
                RootNode::new
        );
    }

    private static Token<MutableRootNode> doctype() {
        return rule(() -> GenericTokens.<MutableRootNode, String, String, String, String>sequence(
                exactText("!!!"),
                whitespace(),
                match(atLeastOneChar(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype),
                (ignored, whitespace, doctype) -> doctype
        ));
    }

    private static Token<MutableRootNode> indentedLine() {
        return rule(() -> GenericTokens.<MutableRootNode, String, Node, Node>sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                GenericTokens.<MutableRootNode, MutableHtmlNode>contextSwitch(
                        MutableHtmlNode::new,
                        lineContent(),
                        MutableRootNode::addNode
                ),
                (indent, node) -> node
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
        return rule(() -> GenericTokens.<MutableHtmlNode, Character, String, Node>sequence(
                singleChar('\\'),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new RubyString(value))),
                (ignored, text) -> new TextNode(new RubyString(text))
        ));
    }

    private static Token<MutableHtmlNode> printExpression() {
        return rule(() -> GenericTokens.<MutableHtmlNode, Character, RubyExpression, Node>relaxedSequence(
                singleChar('='),
                GenericTokens.<MutableHtmlNode, MutableRubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        RubyGrammar.expression(),
                        (node, value) -> node.setContent(value.toExpression())
                ),
                (ignored, expression) -> new TextNode(expression)
        ));
    }

    private static Token<MutableHtmlNode> htmlTag() {
        return rule(() -> GenericTokens.<MutableHtmlNode, Optional<String>, List<RubyHash>, RubyExpression, MutableHtmlNode>relaxedSequence(
                atMostOne(tagName()),
                anyNumberOf(
                        GenericTokens.<MutableHtmlNode>anyOf(
                                idAttribute(),
                                classAttribute(),
                                RubyGrammar.hash()
                        )
                ),
                match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new RubyString(value))),
                (tagName, attributes, content) -> new MutableHtmlNode(tagName.orElse(null), attributes, content)
        ));
    }

    private static Token<MutableHtmlNode> tagName() {
        return rule(() -> Terminals.<MutableHtmlNode, String>leadingChar(
                '%',
                Predicates.TAG_NAME_CHAR,
                MutableHtmlNode::setTagName,
                (value) -> value
        ));
    }

    private static Token<MutableHtmlNode> idAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode, RubyHash>leadingChar(
                '#',
                Predicates.ID_OR_CLASS_CHAR,
                (node, value) -> node.setId(new RubyString(value)),
                (value) -> RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(value))
        ));
    }

    private static Token<MutableHtmlNode> classAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode, RubyHash>leadingChar(
                '.',
                Predicates.ID_OR_CLASS_CHAR,
                MutableHtmlNode::addClass,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(value))
        ));
    }
}
