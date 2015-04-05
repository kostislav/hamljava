package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;

public class HamlParser {
    private enum State {
        START,
        TAG_NAME,
        CLASS,
        ID,
        CONTENT
    }

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

                    State state = State.START;
                    int tokenStart = 0;
                    int currentPosition = 0;
                    for (char c : strippedLine.toCharArray()) {
                        currentPosition++;
                        if(states.get(state).isValidChar(c)) {
                            continue;
                        } else {
                            states.get(state).endOfToken(parsingResult, strippedLine.substring(tokenStart, currentPosition - 1));
                        }

                        tokenStart = currentPosition;

                        state = states.get(state).transition(currentPosition, c);
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

    private int leadingTabs(String line) {
        int numTabs = 0;
        while (line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }

    private static class ParsingState {
        private final CharPredicate validChars;
        private final BiConsumer<ParsingResult, String> onEnd;
        private final Map<Character, State> transitions;

        private ParsingState(CharPredicate validChars, BiConsumer<ParsingResult, String> onEnd, Map<Character, State> transitions) {
            this.validChars = validChars;
            this.onEnd = onEnd;
            this.transitions = transitions;
        }

        public boolean isValidChar(char c) {
            return validChars.test(c);
        }

        public void endOfToken(ParsingResult parsingResult, String substring) {
            onEnd.accept(parsingResult, substring);
        }

        public State transition(int currentPosition, char c) throws ParseException {
            State newState = transitions.get(c);
            if (newState == null) {
                throw new ParseException("Could not parse line at position " + currentPosition);
            } else {
                return newState;
            }
        }
    }

    private interface CharPredicate {
        boolean test(char c);
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
