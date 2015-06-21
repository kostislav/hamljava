package cz.judas.jan.haml.output;

import com.google.common.html.HtmlEscapers;

public class HtmlOutput {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final boolean escapeByDefault;

    public HtmlOutput(boolean escapeByDefault) {
        this.escapeByDefault = escapeByDefault;
    }

    public HtmlOutput addChar(char c) {
        stringBuilder.append(c);
        return this;
    }

    public HtmlOutput addUnescaped(Object value) {
        stringBuilder.append(value);
        return this;
    }

    public HtmlOutput addEscaped(Object value) {
        stringBuilder.append(HtmlEscapers.htmlEscaper().escape(value.toString()));
        return this;
    }

    public HtmlOutput add(Object value) {
        if (escapeByDefault) {
            return addEscaped(value);
        } else {
            return addUnescaped(value);
        }
    }

    public HtmlOutput newChild() {
        return new HtmlOutput(escapeByDefault);
    }

    public String build() {
        return stringBuilder.toString();
    }
}
