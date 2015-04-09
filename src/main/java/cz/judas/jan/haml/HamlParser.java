package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.LeadingCharToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.Html5DoctypeNode;
import cz.judas.jan.haml.tree.Node;

import java.util.ArrayDeque;
import java.util.Deque;

import static cz.judas.jan.haml.tokens.AnyNumberOfToken.anyNumberOf;
import static cz.judas.jan.haml.tokens.AnyOfToken.anyOf;

public class HamlParser {
    private final Token<MutableHtmlNode> tagToken = anyNumberOf(
            anyOf(ImmutableList.of(
                    new LeadingCharToken(
                            '%',
                            this::isTagNameChar,
                            MutableHtmlNode::setTagName
                    ),
                    new LeadingCharToken(
                            '.',
                            this::isIdOrClassChar,
                            MutableHtmlNode::addClass
                    ),
                    new LeadingCharToken(
                            '#',
                            this::isIdOrClassChar,
                            MutableHtmlNode::setId
                    ),
                    new LeadingCharToken(
                            ' ',
                            c -> true,
                            MutableHtmlNode::setContent
                    )
            ))
    );

    public String process(String haml) throws ParseException {
        StringBuilder stringBuilder = new StringBuilder();
        Deque<MutableNode> stack = new ArrayDeque<>();
        stack.push(new MutableRootNode());

        for (String line : haml.split("\n")) {
            if (line.startsWith("!!!")) {
                if (line.equals("!!! 5")) {
                    stack.peekFirst().addChild(new Html5DoctypeNode());
                } else {
                    throw new ParseException("Unsupported doctype " + line.substring(4));
                }
            } else {
                int numTabs = leadingTabs(line);

                while (numTabs < stack.size() - 1) {
                    stack.pop();
                }

                MutableHtmlNode mutableHtmlNode = new MutableHtmlNode();

                tagToken.tryEat(line, numTabs, mutableHtmlNode);

                stack.peekFirst().addChild(mutableHtmlNode);
                stack.push(mutableHtmlNode);
            }
        }

        Node rootNode = stack.peekLast().toNode();
        rootNode.appendTo(stringBuilder);

        return stringBuilder.toString();
    }

    private boolean isIdOrClassChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
    }

    private boolean isTagNameChar(char c) {
        return Character.isLetterOrDigit(c);
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while (line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }
}
