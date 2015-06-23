package cz.judas.jan.haml.output;

import com.google.common.collect.ImmutableSet;
import com.google.common.html.HtmlEscapers;

import java.util.Set;
import java.util.function.Consumer;

public class StreamHtmlOutput implements HtmlOutput {
    private static final Set<String> VOID_ELEMENTS = ImmutableSet.of(
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"
    );
    private static final HtmlOutput VOID_OUTPUT = new VoidTagHtmlOutput();

    private final StringBuilder stringBuilder = new StringBuilder();
    private final boolean escapeByDefault;
    private final TagAttributeBuilder addAttribute = this::addAttribute;

    public StreamHtmlOutput(boolean escapeByDefault) {
        this.escapeByDefault = escapeByDefault;
    }

    @Override
    public HtmlOutput addChar(char c) {
        stringBuilder.append(c);
        return this;
    }

    @Override
    public HtmlOutput addUnescaped(Object value) {
        stringBuilder.append(value);
        return this;
    }

    @Override
    public HtmlOutput addEscaped(Object value) {
        stringBuilder.append(HtmlEscapers.htmlEscaper().escape(value.toString()));
        return this;
    }

    @Override
    public HtmlOutput add(Object value) {
        if (escapeByDefault) {
            return addEscaped(value);
        } else {
            return addUnescaped(value);
        }
    }

    @Override
    public HtmlOutput htmlTag(String name, Consumer<TagAttributeBuilder> attributeBuilder, Consumer<HtmlOutput> bodyBuilder) {
        stringBuilder.append('<').append(name);
        attributeBuilder.accept(addAttribute);
        if(VOID_ELEMENTS.contains(name)) {
            stringBuilder.append(" />");
            bodyBuilder.accept(VOID_OUTPUT);
        } else {
            stringBuilder.append('>');
            bodyBuilder.accept(this);
            stringBuilder.append("</").append(name).append('>');
        }
        return this;
    }

    @Override
    public HtmlOutput newChild() {
        return new StreamHtmlOutput(escapeByDefault);
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
