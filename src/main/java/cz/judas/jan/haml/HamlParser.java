package cz.judas.jan.haml;

import org.apache.commons.lang.StringUtils;

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
                String strippedLine = StringUtils.stripStart(line, "\t");
                stack.push(strippedLine);
                stringBuilder.append('<').append(strippedLine).append(">");
            }
        }

        while(!stack.isEmpty()) {
            stringBuilder.append("</").append(stack.pop()).append(">");
        }

        return stringBuilder.toString();
    }
}
