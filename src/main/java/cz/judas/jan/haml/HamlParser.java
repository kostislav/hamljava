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
        Deque<String> stack = new ArrayDeque<>();

        for (String line : haml.split("\n")) {
            if (line.startsWith("!!!")) {
                if (line.equals("!!! 5")) {
                    stringBuilder.append("<!DOCTYPE html>\n");
                } else {
                    throw new ParseException("Unsupported doctype " + line.substring(4));
                }
            } else {
                int numTabs = leadingTabs(line);
                String strippedLine = line.substring(numTabs);

                while (numTabs < stack.size()) {
                    closeTag(stringBuilder, stack);
                }

                if (strippedLine.startsWith("%")) {
                    String tagName = null;
                    Set<String> classes = null;
                    Map<String, String> attributes = null;
                    String content = "";

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
                                    tagName = strippedLine.substring(tokenStart, currentPosition - 1);
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
                                    if (classes == null) {
                                        classes = new LinkedHashSet<>();
                                    }
                                    classes.add(strippedLine.substring(tokenStart, currentPosition - 1));
                                    tokenStart = currentPosition;
                                } else {
                                    if (classes == null) {
                                        classes = new LinkedHashSet<>();
                                    }
                                    classes.add(strippedLine.substring(tokenStart, currentPosition - 1));

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
                                    if(attributes == null) {
                                        attributes = new LinkedHashMap<>();
                                    }
                                    attributes.put("id", strippedLine.substring(tokenStart, currentPosition - 1));

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
                            tagName = strippedLine.substring(tokenStart, currentPosition);
                            break;
                        case CLASS:
                            if (classes == null) {
                                classes = new LinkedHashSet<>();
                            }
                            classes.add(strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case ID:
                            if(attributes == null) {
                                attributes = new LinkedHashMap<>();
                            }
                            attributes.put("id", strippedLine.substring(tokenStart, currentPosition));
                            break;
                        case CONTENT:
                            content = strippedLine.substring(tokenStart, currentPosition);
                            break;
                        case START:
                            throw new ParseException("Could not parse line " + line);
                    }
                    if(classes != null) {
                        if(attributes == null) {
                            attributes = new LinkedHashMap<>();
                        }
                        attributes.put("class", StringUtils.join(classes, ' '));
                    }
                    if(attributes == null) {
                        attributes = Collections.emptyMap();
                    }

                    htmlTag(stringBuilder, tagName, attributes, content);
                    stack.push(tagName);

                } else {
                    throw new ParseException("Could not parse line " + line);
                }
            }
        }

        while (!stack.isEmpty()) {
            closeTag(stringBuilder, stack);
        }

        return stringBuilder.toString();
    }

    private void htmlTag(StringBuilder stringBuilder, String tagName, Map<String, String> attributes, String content) {
        stringBuilder.append('<').append(tagName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            stringBuilder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
        }
        stringBuilder.append('>').append(content);
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while (line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }

    private void closeTag(StringBuilder stringBuilder, Deque<String> stack) {
        stringBuilder.append("</").append(stack.pop()).append('>');
    }
}
