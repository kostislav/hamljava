package cz.judas.jan.hamljava.output;

import com.google.common.collect.ImmutableSet;
import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.function.Consumer;

public class StreamHtmlOutput implements HtmlOutput {
    private static final Set<String> VOID_ELEMENTS = ImmutableSet.of(
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"
    );
    private static final HtmlOutput VOID_OUTPUT = new VoidTagHtmlOutput();

    private final QuietWriter writer;
    private final boolean escapeByDefault;
    private final TagAttributeBuilder addAttribute = this::addAttribute;

    public StreamHtmlOutput(Writer writer, boolean escapeByDefault) {
        this.writer = new QuietWriter(writer);
        this.escapeByDefault = escapeByDefault;
    }

    @Override
    public HtmlOutput addChar(char c) {
        writer.append(c);
        return this;
    }

    @Override
    public HtmlOutput addUnescaped(Object value) {
        writer.append(value.toString());
        return this;
    }

    @Override
    public HtmlOutput addEscaped(Object value) {
        writer.append(HtmlEscapers.htmlEscaper().escape(value.toString()));
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
        writer.append('<').append(name);
        attributeBuilder.accept(addAttribute);
        if(VOID_ELEMENTS.contains(name)) {
            writer.append(" />");
            bodyBuilder.accept(VOID_OUTPUT);
        } else {
            writer.append('>');
            bodyBuilder.accept(this);
            writer.append("</").append(name).append('>');
        }
        return this;
    }

    private void addAttribute(String name, Object value) {
        String actualValue;
        if(value instanceof Boolean) {
            if(value.equals(Boolean.TRUE)) {
                actualValue = name;
            } else {
                return;
            }
        } else {
            actualValue = value.toString();
        }
        writer.append(' ').append(name).append("=\"");
        add(actualValue);
        writer.append('"');
    }

    private static class QuietWriter {
        private final Writer writer;

        private QuietWriter(Writer writer) {
            this.writer = writer;
        }

        public QuietWriter append(char c) {
            try {
                writer.append(c);
                return this;
            } catch (IOException e) {
                throw new RuntimeException("Could not write", e);
            }
        }

        public QuietWriter append(String s) {
            try {
                writer.append(s);
                return this;
            } catch (IOException e) {
                throw new RuntimeException("Could not write", e);
            }
        }
    }
}
