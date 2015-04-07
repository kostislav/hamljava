package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public class HamlParser {
    private final Map<State, ParsingState> states = ImmutableMap.<State, ParsingState>builder()
            .put(State.TAG_NAME, new ParsingState(
                    '%',
                    this::isTagNameChar,
                    ParsingResult::setTagName,
                    ImmutableSet.of(State.CLASS, State.ID, State.CONTENT)
            ))
            .put(State.CLASS, new ParsingState(
                    '.',
                    this::isIdOrClassChar,
                    ParsingResult::addClass,
                    ImmutableSet.of(State.CLASS, State.ID, State.CONTENT)
            ))
            .put(State.ID, new ParsingState(
                    '#',
                    this::isIdOrClassChar,
                    (parsingResult, substring) -> parsingResult.addAttribute("id", substring),
                    ImmutableSet.of(State.CLASS, State.CONTENT)
            ))
            .put(State.CONTENT, new ParsingState(
                    ' ',
                    c -> true,
                    ParsingResult::setContent,
                    Collections.emptySet()
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

                while (numTabs < stack.size() - 1) {
                    stack.pop();
                }

                ParsingResult parsingResult = new ParsingResult();

                int currentPosition = numTabs;
                Set<State> allowedStates = ImmutableSet.copyOf(State.values());

                while (currentPosition != line.length()) {
                    boolean found = false;
                    for (State candidate : allowedStates) {
                        ParsingState candidateState = states.get(candidate);
                        int newPosition = candidateState.tryEat(line, currentPosition, parsingResult);
                        if(newPosition != -1) {
                            allowedStates = candidateState.getFollowingStates();
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
