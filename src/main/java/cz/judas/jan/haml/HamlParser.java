package cz.judas.jan.haml;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

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
                String tagDef;
                String content;
                if(spaceIndex == -1) {
                    tagDef = strippedLine;
                    content = "";
                } else {
                    tagDef = strippedLine.substring(0, spaceIndex);
                    content = strippedLine.substring(spaceIndex + 1);
                }
                int dotIndex = tagDef.indexOf('.');
                String tagName;
                Map<String, String> attributes = new LinkedHashMap<>();
                if(dotIndex != -1) {
                    tagName = tagDef.substring(0, dotIndex);
                    attributes.put("class", StringUtils.replace(tagDef.substring(dotIndex + 1), ".", " "));
                } else {
                    tagName = tagDef;
                }
                htmlTag(stringBuilder, content, tagName, attributes);
                stack.push(tagName);
            }
        }

        while(!stack.isEmpty()) {
            closeTag(stringBuilder, stack);
        }

        return stringBuilder.toString();
    }

    private void htmlTag(StringBuilder stringBuilder, String content, String tagName, Map<String, String> attributes) {
        stringBuilder.append('<').append(tagName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            stringBuilder.append(' ').append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');
        }
        stringBuilder.append('>').append(content);
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
}
