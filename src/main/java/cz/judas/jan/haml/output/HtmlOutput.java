package cz.judas.jan.haml.output;

import java.util.function.Consumer;

public interface HtmlOutput {
    HtmlOutput addChar(char c);

    HtmlOutput addUnescaped(Object value);

    HtmlOutput addEscaped(Object value);

    HtmlOutput add(Object value);

    HtmlOutput htmlTag(String name, Consumer<TagAttributeBuilder> attributeBuilder, Consumer<HtmlOutput> bodyBuilder);

    HtmlOutput newChild();
}
