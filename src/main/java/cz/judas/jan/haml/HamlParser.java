package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.mutabletree.MutableHtmlNode;
import cz.judas.jan.haml.mutabletree.MutableNode;
import cz.judas.jan.haml.mutabletree.MutableRootNode;
import cz.judas.jan.haml.tokens.DoctypeToken;
import cz.judas.jan.haml.tokens.LeadingCharToken;
import cz.judas.jan.haml.tokens.Token;
import cz.judas.jan.haml.tree.Node;

import static cz.judas.jan.haml.tokens.AnyNumberOfToken.anyNumberOf;
import static cz.judas.jan.haml.tokens.AnyOfToken.anyOf;

public class HamlParser {
    private final Token<MutableNode> doctypeToken = new DoctypeToken();
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
        MutableRootNode rootNode = new MutableRootNode();

        for (String line : haml.split("\n")) {
            if(doctypeToken.tryEat(line, 0, rootNode) == -1) {
                int numTabs = leadingTabs(line);

                while (numTabs < rootNode.nestingLevel()) {
                    rootNode.levelUp();
                }

                MutableHtmlNode mutableHtmlNode = new MutableHtmlNode();

                tagToken.tryEat(line, numTabs, mutableHtmlNode);

                rootNode.addNode(mutableHtmlNode);
            }
        }

        Node finalNode = rootNode.toNode();
        StringBuilder stringBuilder = new StringBuilder();
        finalNode.appendTo(stringBuilder);

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
