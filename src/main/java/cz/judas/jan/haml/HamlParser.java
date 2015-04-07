package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class HamlParser {
    private final List<ParsingState> tokens = ImmutableList.of(
            new ParsingState(
                    '%',
                    this::isTagNameChar,
                    ParsingResult::setTagName
            ),
            new ParsingState(
                    '.',
                    this::isIdOrClassChar,
                    ParsingResult::addClass
            ),
            new ParsingState(
                    '#',
                    this::isIdOrClassChar,
                    ParsingResult::setId
            ),
            new ParsingState(
                    ' ',
                    c -> true,
                    ParsingResult::setContent
            )
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

                int currentPosition = numTabs;

                while (currentPosition != line.length()) {
                    boolean found = false;
                    for (ParsingState candidateState : tokens) {
                        int newPosition = candidateState.tryEat(line, currentPosition, parsingResult);
                        if(newPosition != -1) {
                            currentPosition = newPosition;
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        throw new ParseException("Could not parse line");
                    }
                }

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
