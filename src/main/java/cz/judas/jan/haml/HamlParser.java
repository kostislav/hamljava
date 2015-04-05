package cz.judas.jan.haml;

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
                                if (c == '%') {
                                    state = State.TAG_NAME;
                                    tokenStart = currentPosition;
                                }
                                break;
                            case TAG_NAME:
                                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                                    continue;
                                } else {
                                    parsingResult.setTagName(strippedLine.substring(tokenStart, currentPosition - 1));
                                    tokenStart = currentPosition;

                                    if (c == '.') {
                                        state = State.CLASS;
                                    } else if (c == '#') {
                                        state = State.ID;
                                    } else if (c == ' ') {
                                        state = State.CONTENT;
                                    } else {
                                        throw new ParseException("Could not parse line at position " + currentPosition);
                                    }
                                }
                                break;
                            case CLASS:
                                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_') {
                                    continue;
                                } else if (c == '.') {
                                    parsingResult.addClass(strippedLine.substring(tokenStart, currentPosition - 1));
                                    tokenStart = currentPosition;
                                } else {
                                    parsingResult.addClass(strippedLine.substring(tokenStart, currentPosition - 1));

                                    tokenStart = currentPosition;
                                    if (c == '#') {
                                        state = State.ID;
                                    } else if (c == ' ') {
                                        state = State.CONTENT;
                                    } else {
                                        throw new ParseException("Could not parse line at position " + currentPosition);
                                    }
                                }
                                break;
                            case ID:
                                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_') {
                                    continue;
                                } else if (c == '#') {
                                    throw new ParseException("Multiple IDs not supported");
                                } else {
                                    parsingResult.addAttribute("id", strippedLine.substring(tokenStart, currentPosition - 1));

                                    tokenStart = currentPosition;
                                    if (c == '.') {
                                        state = State.CLASS;
                                    } else if (c == ' ') {
                                        state = State.CONTENT;
                                    } else {
                                        throw new ParseException("Could not parse line at position " + currentPosition);
                                    }
                                }
                                break;
                        }
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
            if(attributes == null) {
                attributes = new LinkedHashMap<>();
            }
            attributes.put(name, value);
        }

        public void addClass(String name) {
            if(classes == null) {
                classes = new LinkedHashSet<>();
            }
            classes.add(name);
        }

        public Map<String, String> getAttributes() {
            if(attributes == null) {
                if(classes == null) {
                    return Collections.emptyMap();
                } else {
                    return Collections.singletonMap("class", StringUtils.join(classes, ' '));
                }
            } else {
                if(classes == null) {
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
