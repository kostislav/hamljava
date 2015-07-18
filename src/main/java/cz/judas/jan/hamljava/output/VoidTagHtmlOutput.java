package cz.judas.jan.hamljava.output;

import java.util.function.Consumer;

public class VoidTagHtmlOutput implements HtmlOutput {
    private static final String ERROR_MESSAGE = "Void tags cannot have content";

    @Override
    public HtmlOutput addChar(char c) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public HtmlOutput addUnescaped(Object value) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public HtmlOutput addEscaped(Object value) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public HtmlOutput add(Object value) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public HtmlOutput htmlTag(String name, Consumer<TagAttributeBuilder> attributeBuilder, Consumer<HtmlOutput> bodyBuilder) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }
}
