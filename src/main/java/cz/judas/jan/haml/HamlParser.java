package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;

public class HamlParser {
    private final Map<State, ParsingState> states = ImmutableMap.<State, ParsingState>builder()
            .put(State.START, new ParsingState(
                    c -> false,
                    (parsingResult, substring) -> {
                    },
                    ImmutableMap.of(
                            '%', State.TAG_NAME
                    )
            ))
            .put(State.TAG_NAME, new ParsingState(
                    this::isTagNameChar,
                    ParsingResult::setTagName,
                    ImmutableMap.of(
                            '.', State.CLASS,
                            '#', State.ID,
                            ' ', State.CONTENT
                    )
            ))
            .put(State.CLASS, new ParsingState(
                    this::isIdOrClassChar,
                    ParsingResult::addClass,
                    ImmutableMap.of(
                            '.', State.CLASS,
                            '#', State.ID,
                            ' ', State.CONTENT
                    )
            ))
            .put(State.ID, new ParsingState(
                    this::isIdOrClassChar,
                    (parsingResult, substring) -> parsingResult.addAttribute("id", substring),
                    ImmutableMap.of(
                            '.', State.CLASS,
                            ' ', State.CONTENT
                    )
            ))
            .put(State.CONTENT, new ParsingState(
                    c -> true,
                    ParsingResult::setContent,
                    Collections.emptyMap()
            ))
            .build();

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
                String strippedLine = line.substring(numTabs);

                while (numTabs < stack.size() - 1) {
                    stack.pop();
                }

                if (strippedLine.startsWith("%")) {
                    ParsingResult parsingResult = new ParsingResult();

                    ParsingState state = states.get(State.START);
                    int currentPosition = 0;
                    while (true) {
                        StateTransition stateTransition = state.eat(strippedLine, currentPosition, parsingResult);
                        if (stateTransition.getNewState() == State.END) {
                            break;
                        } else {
                            state = states.get(stateTransition.getNewState());
                            currentPosition = stateTransition.getNewPosition();
                        }
                    }

                    HtmlNode node = parsingResult.toHtmlNode();
                    stack.peekFirst().addChild(node);
                    stack.push(node);

                } else {
                    throw new ParseException("Could not parse line " + line);
                }
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
