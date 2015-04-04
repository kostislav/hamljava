package cz.judas.jan.haml;

import java.util.ArrayDeque;
import java.util.Deque;

public class HamlParser {
    public String process(String haml) {
        StringBuilder stringBuilder = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();

        for (String line : haml.split("\n")) {
            if(line.equals("!!! 5")) {
                stringBuilder.append("<!DOCTYPE html>\n");
            } else {
                int numTabs = leadingTabs(line);
                String strippedLine = line.substring(numTabs);

                while(numTabs < stack.size()) {
                    closeTag(stringBuilder, stack);
                }

                int spaceIndex = strippedLine.indexOf(' ');
                String tagName;
                if(spaceIndex == -1) {
                    tagName = strippedLine;
                    stringBuilder.append('<').append(strippedLine).append(">");
                } else {
                    tagName = strippedLine.substring(0, spaceIndex);
                    stringBuilder.append('<').append(tagName).append(">").append(strippedLine.substring(spaceIndex + 1));
                }
                stack.push(tagName);
            }
        }

        while(!stack.isEmpty()) {
            closeTag(stringBuilder, stack);
        }

        return stringBuilder.toString();
    }

    private int leadingTabs(String line) {
        int numTabs = 0;
        while(line.charAt(numTabs) == '\t') {
            numTabs++;
        }
        return numTabs;
    }

    private void closeTag(StringBuilder stringBuilder, Deque<String> stack) {
        stringBuilder.append("</").append(stack.pop()).append(">");
    }
}
