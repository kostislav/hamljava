package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.tokens.Token;
import cz.judas.jan.haml.predicates.Predicates;
import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import cz.judas.jan.haml.tree.ruby.RubyString;
import cz.judas.jan.haml.tree.ruby.RubySymbol;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;

import static cz.judas.jan.haml.parser.tokens.TokenCache.rule;
import static cz.judas.jan.haml.parser.tokens.generic.GenericTokens.*;
import static cz.judas.jan.haml.parser.tokens.terminal.Terminals.*;

public class HamlGrammar {
    private final RubyGrammar rubyGrammar = new RubyGrammar();

    public Token<String> doctype() {
        return rule(() -> sequence(
                exactText("!!!"),
                whitespace(),
                atLeastOneChar(Character::isLetterOrDigit),
                (ignored, whitespace, doctype) -> doctype
        ));
    }

    public Token<String> indent() {
        return anyNumberOfChars('\t');
    }

    public Token<MutableHtmlNode> lineContent() {
        return rule(() -> anyOf(
                line(escapedPlainText()),
                line(htmlTag()),
                line(transformation(textContent(), MutableHtmlNode::textNode))
        ));
    }

    private Token<MutableHtmlNode> escapedPlainText() {
        return rule(() -> sequence(
                singleChar('\\'),
                textContent(),
                (ignored, text) -> MutableHtmlNode.textNode(text)
        ));
    }

    private Token<MutableHtmlNode> htmlTag() {
        return rule(() -> sequence(
                atMostOne(tagName()),
                anyNumberOf(
                        anyOf(
                                idAttribute(),
                                classAttribute(),
                                rubyGrammar.hash()
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

    private Token<RubyExpression> printExpression() {
        return rule(() -> relaxedSequence(
                singleChar('='),
                rubyGrammar.expression(),
                (ignored, expression) -> expression
        ));
    }

    private Token<RubyString> textContent() {
        return rule(() -> transformation(
                anyNumberOfChars(notNewLine()),
                RubyString::new
        ));
    }

    private Token<String> tagName() {
        return rule(() -> leadingChar(
                '%',
                Predicates.TAG_NAME_CHAR,
                (value) -> value
        ));
    }

    private Token<RubyHash> idAttribute() {
        return rule(() -> leadingChar(
                '#',
                Predicates.ID_OR_CLASS_CHAR,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("id"), new RubyString(value))
        ));
    }

    private Token<RubyHash> classAttribute() {
        return rule(() -> leadingChar(
                '.',
                Predicates.ID_OR_CLASS_CHAR,
                (value) -> RubyHash.singleEntryHash(new RubySymbol("class"), new RubyString(value))
        ));
    }
}
