package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class HamlParser {
    private enum State {
        START,
        TAG_NAME,
        CLASS,
        ID,
        CONTENT
    }

    private final Map<State, Map<Character, State>> transitions = ImmutableMap.<State, Map<Character, State>>of(
            State.START, ImmutableMap.of(
            '%', State.TAG_NAME
    ),
            State.TAG_NAME, ImmutableMap.of(
            '.', State.CLASS,
            '#', State.ID,
            ' ', State.CONTENT
    ),
            State.CLASS, ImmutableMap.of(
            '.', State.CLASS,
            '#', State.ID,
            ' ', State.CONTENT
    ),
            State.ID, ImmutableMap.of(
            '.', State.CLASS,
            ' ', State.CONTENT
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
                String strippedLine = line.substring(numTabs);

                while (numTabs < stack.size() - 1) {
                    stack.pop();
                }

                if (strippedLine.startsWith("%")) {
                    ParsingResult parsingResult = new ParsingResult();

                    State state = State.START;
                    int tokenStart = 0;
                    int currentPosition = 0;
                    for (char c : strippedLine.toCharArray()) {
                        currentPosition++;
                        switch (state) {
                            case START:
                                break;
                            case TAG_NAME:
                                if (isTagNameChar(c)) {
                                    continue;
                                } else {
                                    parsingResult.setTagName(strippedLine.substring(tokenStart, currentPosition - 1));
                                }
                                break;
                            case CLASS:
                                if (isIdOrClassChar(c)) {
                                    continue;
                                } else {
                                    parsingResult.addClass(strippedLine.substring(tokenStart, currentPosition - 1));
                                }
                                break;
                            case ID:
                                if (isIdOrClassChar(c)) {
                                    continue;
                                } else {
                                    parsingResult.addAttribute("id", strippedLine.substring(tokenStart, currentPosition - 1));
                                }
                                break;
                            case CONTENT:
                                continue;
                        }

                        tokenStart = currentPosition;

                        state = transition(state, currentPosition, c);
                    }

                    switch (state) {
                        case TAG_NAME:
                            parsingResult.setTagName(strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case CLASS:
                            parsingResult.addClass(strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case ID:
                            parsingResult.addAttribute("id", strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case CONTENT:
                            parsingResult.setContent(strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case START:
                            throw new ParseException("Could not parse line " + line);
                    }

                    HtmlNode node = new HtmlNode(
                            parsingResult.getTagName(),
                            parsingResult.getAttributes(),
                            parsingResult.getContent()
                    );
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

    private State transition(State state, int currentPosition, char c) throws ParseException {
        State newState = transitions.get(state).get(c);
        if(newState == null) {
            throw new ParseException("Could not parse line at position " + currentPosition);
        } else {
            state = newState;
        }
        return state;
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while (line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }

    private static class ParsingResult {
        private String tagName = null;
        private Map<String, String> attributes = null;
        private Set<String> classes = null;
        private String content = "";

        private String getTagName() {
            return tagName;
        }

        private void setTagName(String tagName) {
            this.tagName = tagName;
        }

        private String getContent() {
            return content;
        }

        private void setContent(String content) {
            this.content = content;
        }

        public void addAttribute(String name, String value) {
            if (attributes == null) {
                attributes = new LinkedHashMap<>();
            }
            attributes.put(name, value);
        }

        public void addClass(String name) {
            if (classes == null) {
                classes = new LinkedHashSet<>();
            }
            classes.add(name);
        }

        public Map<String, String> getAttributes() {
            if (attributes == null) {
                if (classes == null) {
                    return Collections.emptyMap();
                } else {
                    return Collections.singletonMap("class", StringUtils.join(classes, ' '));
                }
            } else {
                if (classes == null) {
                    return attributes;
                } else {
                    Map<String, String> copy = new LinkedHashMap<>(attributes);
                    copy.put("class", StringUtils.join(classes, ' '));
                    return copy;
                }
            }
        }
    }
}
