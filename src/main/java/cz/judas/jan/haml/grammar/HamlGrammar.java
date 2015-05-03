package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.tokens.TypedToken;
import cz.judas.jan.haml.parser.tokens.generic.ContextSwitchToken2;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.RubyExpression;
import cz.judas.jan.haml.tree.RubyHash;
import cz.judas.jan.haml.tree.RubyString;
import cz.judas.jan.haml.tree.RubySymbol;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import cz.judas.jan.haml.tree.mutable.MutableRootNode;

import java.util.Optional;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar implements Grammar<MutableRootNode, Optional<String>> {

    @Override
    public TypedToken<MutableRootNode, Optional<String>> buildRules() {
        return sequence(
                atMostOne(line(doctype())),
                anyNumberOf(indentedLine()),
                (doctype, children) -> doctype
        );
    }

    private TypedToken<MutableRootNode, String> doctype() {
        return rule(() -> sequence(
                exactText("!!!"),
                whitespace(),
                atLeastOneChar(Character::isLetterOrDigit),
                (ignored, whitespace, doctype) -> doctype
        ));
    }

    private TypedToken<MutableRootNode, MutableHtmlNode> indentedLine() {
        return rule(() -> sequence(
                match(anyNumberOf('\t'), MutableRootNode.class).to(MutableRootNode::levelUp),
                new ContextSwitchToken2<>(
                        lineContent(),
                        MutableRootNode::addNode
                ),
                (indent, node) -> node
        ));
    }

    private TypedToken<Object, MutableHtmlNode> lineContent() {
        return rule(() -> anyOf(
                line(escapedPlainText()),
                line(htmlTag()),
                line(transformation(textContent(), MutableHtmlNode::textNode))
        ));
    }

    private TypedToken<Object, MutableHtmlNode> escapedPlainText() {
        return rule(() -> sequence(
                singleChar('\\'),
                textContent(),
                (ignored, text) -> MutableHtmlNode.textNode(text)
        ));
    }

    private TypedToken<Object, MutableHtmlNode> htmlTag() {
        return rule(() -> sequence(
                atMostOne(tagName()),
                anyNumberOf(
                        anyOf(
                                idAttribute(),
                                classAttribute(),
                                RubyGrammar.hash()
                        )
                ),
                atMostOne(
                        anyOf(
                                printExpression(),
                                sequence(
                                        singleChar(' '),
                                        textContent(),
                                        (ignored, content) -> content
                                )
                        )
                ),
                (tagName, attributes, content) -> new MutableHtmlNode(tagName.orElse(null), attributes, content.orElse(RubyString.EMPTY))
        ));
    }

    private TypedToken<Object, RubyExpression> printExpression() {
        return rule(() -> relaxedSequence(
                singleChar('='),
                RubyGrammar.expression(),
                (ignored, expression) -> expression
        ));
    }

    private TypedToken<Object, RubyString> textContent() {
        return rule(() -> transformation(
                anyNumberOf(notNewLine()),
                RubyString::new
        ));
    }

    private TypedToken<Object, String> tagName() {
        return rule(() -> leadingChar(
                '%',
                Predicates.TAG_NAME_CHAR,
                (value) -> value
        ));
    }

    private TypedToken<Object, RubyHash> idAttribute() {
        return rule(() -> leadingChar(
                '#',
                Predicates.ID_OR_CLASS_CHAR,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(value))
        ));
    }

    private TypedToken<Object, RubyHash> classAttribute() {
        return rule(() -> leadingChar(
                '.',
                Predicates.ID_OR_CLASS_CHAR,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(value))
        ));
    }
}
