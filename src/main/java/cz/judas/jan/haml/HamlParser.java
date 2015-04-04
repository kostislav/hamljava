package cz.judas.jan.haml;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.function.Consumer;

public class HamlParser {
    public String process(String haml) {
        StringBuilder stringBuilder = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();

        for (String line : haml.split("\n")) {
            if(line.startsWith("!!!")) {
                if(line.equals("!!! 5")) {
                    stringBuilder.append("<!DOCTYPE html>\n");
                } else {
                    throw new IllegalArgumentException("Unsupported doctype " + line.substring(4));
                }
            } else {
                int numTabs = leadingTabs(line);
                String strippedLine = line.substring(numTabs);

                while(numTabs < stack.size()) {
                    closeTag(stringBuilder, stack);
                }

                ParsedLine parsedLine = new ParsedLine(strippedLine);

                parsedLine.updateOn(' ', parsedLine::setContent);
                parsedLine.updateOn('.', s -> parsedLine.addAttribute("class", StringUtils.replace(s, ".", " ")));
                parsedLine.updateOn('#', s -> parsedLine.addAttribute("id", s));

                htmlTag(stringBuilder, parsedLine);
                stack.push(parsedLine.tagName);
            }
        }

        while(!stack.isEmpty()) {
            closeTag(stringBuilder, stack);
        }

        return stringBuilder.toString();
    }

    private void htmlTag(StringBuilder stringBuilder, ParsedLine parsedLine) {
        stringBuilder.append('<').append(parsedLine.tagName);
        for (Map.Entry<String, String> entry : parsedLine.getAttributes().entrySet()) {
            stringBuilder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
        }
        stringBuilder.append('>').append(parsedLine.content);
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while(line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }

    private void closeTag(StringBuilder stringBuilder, Deque<String> stack) {
        stringBuilder.append("</").append(stack.pop()).append('>');
    }

    private static class ParsedLine {
        private String tagName;
        private Map<String, String> attributes = null;
        private String content = "";

        private ParsedLine(String tagName) {
            this.tagName = tagName.substring(1);
        }

        public void addAttribute(String name, String value) {
            if(attributes == null) {
                attributes = new LinkedHashMap<>();
            }
            attributes.put(name, value);
        }

        public Map<String, String> getAttributes() {
            if(attributes == null) {
                return Collections.emptyMap();
            } else {
                return attributes;
            }
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void updateOn(char symbolToFind, Consumer<String> update) {
            int symbolIndex = tagName.indexOf(symbolToFind);
            if(symbolIndex != -1) {
                String toBeSplit = tagName;
                tagName = toBeSplit.substring(0, symbolIndex);
                update.accept(toBeSplit.substring(symbolIndex + 1));
            }
        }
    }
}
