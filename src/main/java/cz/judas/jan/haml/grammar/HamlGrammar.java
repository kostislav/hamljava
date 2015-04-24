package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.generic.GenericTokens;
import cz.judas.jan.haml.parser.tokens.terminal.Terminals;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.*;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;
import cz.judas.jan.haml.tree.mutable.MutableRubyExpression;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar implements Grammar<MutableRootNode, RootNode> {

    @Override
    public TypedToken<MutableRootNode, RootNode> buildRules() {
        return GenericTokens.<MutableRootNode, Optional<String>, List<Node>, RootNode>sequence(
                atMostOne(line(doctype())),
                anyNumberOf(indentedLine()),
                RootNode::new
        );
    }

    private static TypedToken<MutableRootNode, String> doctype() {
        return rule(() -> GenericTokens.<MutableRootNode, String, String, String, String>sequence(
                exactText("!!!"),
                whitespace(),
                match(atLeastOneChar(Character::isLetterOrDigit), MutableRootNode.class).to(MutableRootNode::setDoctype),
                (ignored, whitespace, doctype) -> doctype
        ));
    }

    private static TypedToken<MutableRootNode, MutableHtmlNode> indentedLine() {
        return rule(() -> GenericTokens.<MutableRootNode, String, Node, Node>sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                GenericTokens.<MutableRootNode, MutableHtmlNode, MutableHtmlNode>contextSwitch(
                        MutableHtmlNode::new,
                        lineContent(),
                        MutableRootNode::addNode
                ),
                (indent, node) -> node
        ));
    }

    private static TypedToken<MutableHtmlNode, MutableHtmlNode> lineContent() {
        return rule(() -> anyOf(
                line(escapedPlainText()),
                line(htmlTag()),
                line(GenericTokens.<MutableHtmlNode, RubyString, MutableHtmlNode>transformation(textContent(), content -> new MutableHtmlNode(null, Collections.emptyList(), content)))
        ));
    }

    private static TypedToken<MutableHtmlNode, MutableHtmlNode> escapedPlainText() {
        return rule(() -> GenericTokens.<MutableHtmlNode, Character, String, Node>sequence(
                singleChar('\\'),
                textContent(),
                (ignored, text) -> new TextNode(new RubyString(text))
        ));
    }

    private static TypedToken<MutableHtmlNode, MutableHtmlNode> htmlTag() {
        return rule(() -> GenericTokens.<MutableHtmlNode, Optional<String>, List<RubyHash>, RubyExpression, MutableHtmlNode>sequence(
                atMostOne(tagName()),
                anyNumberOf(
                        GenericTokens.<MutableHtmlNode, RubyHash>anyOf(
                                idAttribute(),
                                classAttribute(),
                                RubyGrammar.hash()
                        )
                ),
                atMostOne(
                        anyOf(
                                printExpression(),
                                GenericTokens.<MutableHtmlNode, Character, String, RubyExpression>sequence(
                                        singleChar(' '),
                                        textContent(),
                                        (ignored, content) -> new RubyString(content)
                                )
                        )
                ),
                (tagName, attributes, content) -> new MutableHtmlNode(tagName.orElse(null), attributes, content)
        ));
    }

    private static TypedToken<MutableHtmlNode, RubyExpression> printExpression() {
        return rule(() -> GenericTokens.<MutableHtmlNode, Character, RubyExpression, RubyExpression>relaxedSequence(
                singleChar('='),
                GenericTokens.<MutableHtmlNode, MutableRubyExpression, RubyExpression>contextSwitch(
                        MutableRubyExpression::new,
                        RubyGrammar.expression(),
                        (node, value) -> node.setContent(value.toExpression())
                ),
                (ignored, expression) -> expression
        ));
    }

    private static TypedToken<MutableHtmlNode, RubyString> textContent() {
        return rule(() -> match(anyNumberOf(notNewLine()), MutableHtmlNode.class).to((node, value) -> node.setContent(new RubyString(value))));
    }

    private static TypedToken<MutableHtmlNode, String> tagName() {
        return rule(() -> Terminals.<MutableHtmlNode, String>leadingChar(
                '%',
                Predicates.TAG_NAME_CHAR,
                MutableHtmlNode::setTagName,
                (value) -> value
        ));
    }

    private static TypedToken<MutableHtmlNode, RubyHash> idAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode, RubyHash>leadingChar(
                '#',
                Predicates.ID_OR_CLASS_CHAR,
                (node, value) -> node.setId(new RubyString(value)),
                (value) -> RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(value))
        ));
    }

    private static TypedToken<MutableHtmlNode, RubyHash> classAttribute() {
        return rule(() -> Terminals.<MutableHtmlNode, RubyHash>leadingChar(
                '.',
                Predicates.ID_OR_CLASS_CHAR,
                MutableHtmlNode::addClass,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(value))
        ));
    }
}
