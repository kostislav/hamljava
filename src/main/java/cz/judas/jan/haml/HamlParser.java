package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;

import java.util.ArrayDeque;
import java.util.Deque;

public class HamlParser {
    private final Token bigBadToken = new AnyNumberOfToken(
            new AnyOfToken(ImmutableList.<Token>of(
                    new LeadingCharToken(
                            '%',
                            this::isTagNameChar,
                            ParsingResult::setTagName
                    ),
                    new LeadingCharToken(
                            '.',
                            this::isIdOrClassChar,
                            ParsingResult::addClass
                    ),
                    new LeadingCharToken(
                            '#',
                            this::isIdOrClassChar,
                            ParsingResult::setId
                    ),
                    new LeadingCharToken(
                            ' ',
                            c -> true,
                            ParsingResult::setContent
                    )
            ))
    );

    public String process(String haml) throws ParseException {
        StringBuilder stringBuilder = new StringBuilder();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(new RootNode());

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

                ParsingResult parsingResult = new ParsingResult();

                bigBadToken.tryEat(line, numTabs, parsingResult);

                HtmlNode node = parsingResult.toHtmlNode();
                stack.peekFirst().addChild(node);
                stack.push(node);
            }
        }

        stack.peekLast().appendTo(stringBuilder);

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
