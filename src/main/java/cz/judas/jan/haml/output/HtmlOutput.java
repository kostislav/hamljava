package cz.judas.jan.haml.output;

import com.google.common.html.HtmlEscapers;

import java.util.function.Consumer;

public class HtmlOutput {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final boolean escapeByDefault;
    private final TagAttributeBuilder addAttribute = this::addAttribute;

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

    public HtmlOutput htmlTag(String name, Consumer<TagAttributeBuilder> attributeBuilder, Consumer<HtmlOutput> bodyBuilder) {
        stringBuilder.append('<').append(name);
        attributeBuilder.accept(addAttribute);
        stringBuilder.append('>');
        bodyBuilder.accept(this);
        stringBuilder.append("</").append(name).append('>');
        return this;
    }

    public HtmlOutput newChild() {
        return new HtmlOutput(escapeByDefault);
    }

    public String build() {
        return stringBuilder.toString();
    }

    private void addAttribute(String name, String value) {
        stringBuilder.append(' ').append(name).append("=\"");
        add(value);
        stringBuilder.append('"');
    }
}
