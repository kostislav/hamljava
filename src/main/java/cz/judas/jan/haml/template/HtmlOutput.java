package cz.judas.jan.haml.template;

public class HtmlOutput {
    private final StringBuilder stringBuilder = new StringBuilder();

    public HtmlOutput add(char c) {
        stringBuilder.append(c);
        return this;
    }

    public HtmlOutput addUnescaped(Object value) {
        stringBuilder.append(value);
        return this;
    }

    public HtmlOutput add(Object value) {
        stringBuilder.append(value);
        return this;
    }

    public HtmlOutput newChild() {
        return new HtmlOutput();
    }

    public String build() {
        return stringBuilder.toString();
    }
}
