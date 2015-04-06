package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;

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
                String strippedLine = line.substring(numTabs);

                while (numTabs < stack.size() - 1) {
                    stack.pop();
                }

                ParsingResult parsingResult = new ParsingResult();

                ParsingState state = initialState(line, strippedLine);

                int currentPosition = 0;
                while (true) {
                    currentPosition = state.eat(strippedLine, currentPosition, parsingResult);
                    if (currentPosition == strippedLine.length()) {
                        break;
                    } else {
                        state = transition(state, strippedLine, currentPosition);
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

    private ParsingState transition(ParsingState state, String line, int position) throws ParseException {
        for (State candidate : state.getFollowingStates()) {
            ParsingState candidateState = states.get(candidate);
            if(candidateState.canParse(line, position)) {
                return candidateState;
            }
        }
        throw new ParseException("Could not parse line");
    }

    private ParsingState initialState(String line, String strippedLine) throws ParseException {
        for (ParsingState candidate : states.values()) {
            if(candidate.canParse(strippedLine, 0)) {
                return candidate;
            }
        }
        throw new ParseException("Could not parse line " + line);
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
